package uz.click.myverdisdk.impl.scan

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.jmrtd.lds.icao.MRZInfo
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.callbacks.VerdiScanListener
import uz.click.myverdisdk.core.callbacks.VerdiUserListener
import uz.click.myverdisdk.model.DocType
import uz.click.myverdisdk.util.DateUtil
import uz.click.myverdisdk.util.GraphicOverlay
import uz.click.myverdisdk.util.PublicMethods
import uz.click.myverdisdk.util.mlkit.VisionImageProcessor
import uz.click.myverdisdk.util.mlkit.text.TextRecognitionProcessor
import uz.click.myverdisdk.util.views.OverlayViewQrCode
import uz.digid.myverdi.mlkit.barcodescanner.BarcodeScannerProcessor
import java.util.concurrent.ExecutionException

class ScanActivity : AppCompatActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback,
    TextRecognitionProcessor.ResultListener,
    CompoundButton.OnCheckedChangeListener
{
    private var previewView: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector? = null
    private var scanListener: VerdiScanListener? = null
    private var isQrCode = false
    private val TAG = this::class.java.name

    companion object {
        const val IS_QR_CODE = "isQrCode"
        const val VERDI_LISTENER = "verdiListener"
        fun getInstance(
            activity: Activity,
            scanListener: VerdiScanListener,
            isQrCode: Boolean = false
        ): Intent {
            return Intent(activity, ScanActivity::class.java).apply {
                putExtra(IS_QR_CODE, isQrCode)
                putExtra(VERDI_LISTENER, scanListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passport_scan)

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        if (!PublicMethods.allPermissionsGranted(this)) {
            PublicMethods.runtimePermissions(this)
        }
        getCameraProvider()

        scanListener = intent.extras?.getSerializable(VERDI_LISTENER) as VerdiScanListener?

        isQrCode = intent.extras?.getBoolean(IS_QR_CODE) ?: false
        previewView = findViewById<PreviewView>(R.id.previewView)
        graphicOverlay = findViewById<GraphicOverlay>(R.id.graphicOverlay)
        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }
        if (isQrCode) {
            findViewById<OverlayViewQrCode>(R.id.qrCodeView).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.clPassport).visibility = View.GONE
        } else {
            findViewById<OverlayViewQrCode>(R.id.qrCodeView).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.clPassport).visibility = View.VISIBLE
        }

    }


    private fun getCameraProvider() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                try {
                    cameraProvider = cameraProviderFuture.get()
                    if (PublicMethods.allPermissionsGranted(this)) {
                        bindAllCameraUseCases()
                    }
                } catch (e: ExecutionException) {
                    Log.e(this::class.java.name, "Unhandled exception", e)
                } catch (e: InterruptedException) {
                    Log.e(this::class.java.name, "Unhandled exception", e)
                }
            }, ContextCompat.getMainExecutor(this)
        )
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (cameraProvider == null) {
            return
        }
        val newLensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        val newCameraSelector =
            CameraSelector.Builder().requireLensFacing(newLensFacing).build()
        try {
            if (cameraProvider!!.hasCamera(newCameraSelector)) {
                lensFacing = newLensFacing
                cameraSelector = newCameraSelector
                bindAllCameraUseCases()
                return
            }
        } catch (e: CameraInfoUnavailableException) {
            // Falls through
        }
    }

    override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.run {
            this.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run {
            this.stop()
        }
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (preview != null) {
            cameraProvider!!.unbind(preview)
        }
        val builder = Preview.Builder()
        preview = builder.build()
        preview!!.setSurfaceProvider(previewView!!.surfaceProvider)
        cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */this, cameraSelector!!, preview)
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (imageAnalysis != null) {
            cameraProvider!!.unbind(imageAnalysis)
        }
        if (imageProcessor != null) {
            imageProcessor!!.stop()
        }
        imageProcessor =
            if (isQrCode) {
                BarcodeScannerProcessor(this, this)
            } else {
                TextRecognitionProcessor(
                    this,
                    TextRecognizerOptions.Builder().build(),
                    this
                )
            }

        val builder = ImageAnalysis.Builder()
        imageAnalysis = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        imageAnalysis?.setAnalyzer(
            ContextCompat.getMainExecutor(this)
        ) { imageProxy: ImageProxy ->
            if (needUpdateGraphicOverlayImageSourceInfo) {
                val isImageFlipped =
                    lensFacing == CameraSelector.LENS_FACING_FRONT
                val rotationDegrees =
                    imageProxy.imageInfo.rotationDegrees
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                    graphicOverlay!!.setImageSourceInfo(
                        imageProxy.width, imageProxy.height, isImageFlipped
                    )
                } else {
                    graphicOverlay!!.setImageSourceInfo(
                        imageProxy.height, imageProxy.width, isImageFlipped
                    )
                }
                needUpdateGraphicOverlayImageSourceInfo = false
            }
            try {
                imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)
            } catch (e: MlKitException) {
                Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
            }
        }
        cameraProvider!!.bindToLifecycle( /* lifecycleOwner= */this,
            cameraSelector!!,
            imageAnalysis
        )
        try {
            val camera = cameraProvider!!.bindToLifecycle(this, cameraSelector!!, preview)
            findViewById<ImageView>(R.id.ivFlashButton).setOnClickListener {
                if (it.tag.equals("of")) {
                    camera.cameraControl.enableTorch(true)
                    it.tag = "on"
                } else {
                    camera.cameraControl.enableTorch(false)
                    it.tag = "of"
                }
            }
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "Permission granted!")
        if (PublicMethods.allPermissionsGranted(this)) {
            bindAllCameraUseCases()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSuccess(mrzInfo: MRZInfo?) {
        Log.i(TAG, "onSuccess")
        mrzInfo?.let {
            VerdiUser.config.documentNumber = mrzInfo.documentNumber
            VerdiUser.config.dateOfExpiry = DateUtil.convertFromMrzDate(mrzInfo.dateOfExpiry)
            VerdiUser.config.birthDate = DateUtil.convertFromMrzDate(mrzInfo.dateOfBirth)
            VerdiUser.config.personalNumber = mrzInfo.personalNumber
            VerdiUser.config.docType = if (isQrCode) {
                DocType.ID_CARD
            } else {
                DocType.PASSPORT
            }
            scanListener?.onScanSuccess()
        }
        finish()
    }


    override fun onError(exp: Exception?) {
    }

    override fun onDetect(results: Boolean) {
    }

}