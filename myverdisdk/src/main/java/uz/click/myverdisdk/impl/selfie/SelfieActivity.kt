package uz.click.myverdisdk.impl.selfie

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.impl.RegisterRequestActivity
import uz.click.myverdisdk.model.info.PersonResult
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.util.rotateImage
import uz.click.myverdisdk.util.toBitmap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SelfieActivity : AppCompatActivity() {
    val KEY_EVENT_ACTION = "key_event_action"
    val KEY_EVENT_EXTRA = "key_event_extra"

    lateinit var previewView: PreviewView
    lateinit var ivCameraSwitch: ImageView
    lateinit var ibCameraCapture: ImageButton
    lateinit var llGifView: LinearLayout

    lateinit var btnOk: AppCompatButton
    lateinit var cameraContainer: ConstraintLayout

    private lateinit var viewFinder: PreviewView
    private lateinit var broadcastManager: LocalBroadcastManager
    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraShutterButton: ImageButton

    companion object {
        const val VERDI_LISTENER = "verdiListener"
        private const val TAG = "SelfieActivity"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        fun getInstance(
            activity: Activity,
        ): Intent {
            return Intent(activity, SelfieActivity::class.java)
        }

        fun getInstanceFromNfcActivity(activity: Activity): Intent {
            val intent = Intent(activity, SelfieActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selfie)

        cameraContainer = findViewById<ConstraintLayout>(R.id.cameraContainer)
        previewView = findViewById<PreviewView>(R.id.previewView)
        ivCameraSwitch = findViewById<ImageView>(R.id.cameraSwitchButton)
        ibCameraCapture = findViewById<ImageButton>(R.id.ibCameraCapture)

        viewFinder = previewView
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder.post {
            displayId = viewFinder.display.displayId
            updateCameraUi()
            setUpCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCameraUi()
        updateCameraSwitchButton()
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            lensFacing = when {
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            updateCameraSwitchButton()

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = viewFinder.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetResolution(Size(1080, 1920))
            .setTargetRotation(rotation)
            .build()

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            preview?.setSurfaceProvider(viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }

        return AspectRatio.RATIO_16_9
    }

    @SuppressLint("RestrictedApi")
    private fun updateCameraUi() {
        ibCameraCapture.setOnClickListener {
            savePictureToMemory()
        }

        ivCameraSwitch.let {
            it.isEnabled = false
            it.setOnClickListener {
                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    CameraSelector.LENS_FACING_FRONT
                }
                bindCameraUseCases()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun savePictureToMemory() {
        imageCapture?.takePicture(
            CameraXExecutors.mainThreadExecutor(),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exception: ImageCaptureException) {
                    Log.e("EEE", "Erorr image capture: ${exception.message}")
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = image.toBitmap()
                    if (bitmap != null) {
                        Verdi.user.imageFaceBase =
                            bitmap.rotateImage(image.imageInfo.rotationDegrees.toFloat())
                        bitmap.rotateImage(image.imageInfo.rotationDegrees.toFloat())?.let {
                            Verdi.stateListener?.onSuccess()
                        }
                        startActivity(RegisterRequestActivity.getInstance(this@SelfieActivity))
                        finish()
                    }
                    super.onCaptureSuccess(image)
                }
            })
    }


    private fun updateCameraSwitchButton() {
        try {
            ivCameraSwitch.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            ivCameraSwitch.isEnabled = false
        }
    }

    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }
}