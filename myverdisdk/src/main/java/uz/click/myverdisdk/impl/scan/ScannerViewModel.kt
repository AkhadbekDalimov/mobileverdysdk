package uz.click.myverdisdk.impl.scan

import android.app.Application
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.ExecutionException

class ScannerViewModel : ViewModel() {
    private var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>? = null
    fun getProcessCameraProvider(context: Application): LiveData<ProcessCameraProvider?>? {
        if (cameraProviderLiveData == null) {
            cameraProviderLiveData = MutableLiveData()
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener(
                    {
                        try {
                            cameraProviderLiveData!!.setValue(cameraProviderFuture.get())
                        } catch (e: ExecutionException) {
                            // Handle any errors (including cancellation) here.
                            Log.e(TAG, "Unhandled exception", e)
                        } catch (e: InterruptedException) {
                            Log.e(TAG, "Unhandled exception", e)
                        }
                    },
                    ContextCompat.getMainExecutor(context))
        }
        return cameraProviderLiveData
    }
    /**
     * Create an instance which interacts with the camera service via the given application context.
     */
    companion object {
        private const val TAG = "CameraXViewModel"
    }
}