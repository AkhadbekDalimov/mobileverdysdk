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

package uz.click.myverdisdk.util.mlkit.text

import android.graphics.*
import android.util.Log
import com.google.mlkit.vision.text.Text
import uz.click.myverdisdk.util.GraphicOverlay
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class TextGraphic constructor(overlay: GraphicOverlay?, private val text: Text) :
  GraphicOverlay.Graphic(overlay) {
  private val rectPaint: Paint = Paint()
  private val textPaint: Paint
  private val labelPaint: Paint


  init {
    rectPaint.color = MARKER_COLOR
    rectPaint.style = Paint.Style.STROKE
    rectPaint.strokeWidth = STROKE_WIDTH
    textPaint = Paint()
    textPaint.color = TEXT_COLOR
    textPaint.textSize = TEXT_SIZE
    labelPaint = Paint()
    labelPaint.color = MARKER_COLOR
    labelPaint.style = Paint.Style.FILL
    // Redraw the overlay, as this graphic has been added.
    postInvalidate()
  }

  /** Draws the text block annotations for position, size, and raw value on the supplied canvas.  */
  override fun draw(canvas: Canvas) {

      Log.d(TAG, "Text is: " + text.text)
    for (textBlock in text.textBlocks) { // Renders the text at the bottom of the box.
      val currentText = textBlock.text
      if (currentText.startsWith("P<") || currentText.startsWith("AA") || (currentText.startsWith("AB") && currentText.length > 12)) {
        Log.d(TAG, "TextBlock text is: " + textBlock.text)
        Log.d(
            TAG,
            "TextBlock boundingbox is: " + textBlock.boundingBox
        )
        Log.d(
            TAG,
            "TextBlock cornerpoint is: " + Arrays.toString(textBlock.cornerPoints)
        )
        for (line in textBlock.lines) {

          val rect = RectF(line.boundingBox)
          // If the image is flipped, the left will be translated to right, and the right to left.
          val x0 = translateX(rect.left)
          val x1 = translateX(rect.right)
          rect.left = min(x0, x1)
          rect.right = max(x0, x1)
          rect.top = translateY(rect.top)
          rect.bottom = translateY(rect.bottom)
          canvas.drawRect(rect, rectPaint)
            canvas.rotate(-90f)

            canvas.drawText(
                line.text,
                rect.left,
                rect.bottom + STROKE_WIDTH,
                textPaint
            )

          for (element in line.elements) {
            Log.d(TAG, "Element text is: " + element.text)
            Log.d(
                TAG,
                "Element boundingbox is: " + element.boundingBox
            )
            Log.d(
                TAG,
                "Element cornerpoint is: " + Arrays.toString(element.cornerPoints)
            )
            Log.d(
                TAG,
                "Element language is: " + element.recognizedLanguage
            )
          }
        }
      }
    }
  }

  companion object {
    private const val TAG = "TextGraphic"
    const val TEXT_COLOR = Color.GREEN
    const val MARKER_COLOR = Color.WHITE
    const val TEXT_SIZE = 32.0f
    const val STROKE_WIDTH = 2.0f
  }
}
