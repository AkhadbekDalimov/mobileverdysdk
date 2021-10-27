/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uz.click.myverdisdk.util.mlkit.barcodescanner

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import org.jmrtd.lds.icao.MRZInfo
import uz.click.myverdisdk.util.mlkit.VisionProcessorBase
import uz.click.myverdisdk.util.mlkit.text.TextRecognitionProcessor

/** Barcode Detector Demo.  */
class BarcodeScannerProcessor(context: Context, private val resultListener: TextRecognitionProcessor.ResultListener) :
  VisionProcessorBase<List<Barcode>>(context) {

  private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

  override fun stop() {
    super.stop()
    barcodeScanner.close()
  }

  override fun detectInImage(image: InputImage): Task<List<Barcode>> {
    return barcodeScanner.process(image)
  }

  override fun onSuccess(results: List<Barcode>) {
    if (results.isEmpty()) {
      Log.v(MANUAL_TESTING_LOG, "No barcode has been detected")
    } else {
      val barcode = results[0]
      try {
        val mrz = MRZInfo(barcode.rawValue)
        resultListener.onSuccess(mrz)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Barcode detection failed $e")
  }

  companion object {
    private const val TAG = "BarcodeProcessor"
  }
}
