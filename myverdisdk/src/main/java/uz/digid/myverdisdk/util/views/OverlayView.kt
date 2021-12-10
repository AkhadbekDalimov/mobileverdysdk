package uz.digid.myverdisdk.util.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.annotation.Nullable


class OverlayView : View {
    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        init(Color.parseColor("#03000000"), width/10f, height/8f)
    }
    private var transparentBackground: Paint? = null
    private var eraser: Paint? = null
    private var borderPaint: Paint? = null
    private var horizontalMargin = 0f
    private var verticalMargin = 0f
    private val WIDTH_FACTOR = 2.9f
    private val HEIGHT_FACTOR = 12f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

     @SuppressLint("DrawAllocation")
     override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (horizontalMargin == 0f) {
            horizontalMargin = measuredWidth / WIDTH_FACTOR
        }
        if (verticalMargin == 0f) {
            verticalMargin = measuredHeight / HEIGHT_FACTOR
        }
        val rect = Rect(0, 0, measuredWidth, measuredHeight)
         transparentBackground?.let { canvas.drawRect(rect, it) }
        canvas.drawArc(
            RectF(
                horizontalMargin,
                verticalMargin,
                measuredWidth - horizontalMargin,
                measuredHeight - verticalMargin * 2
            ), 0f, 360f, true, borderPaint!!
        )
        canvas.drawArc(
            RectF(
                horizontalMargin,
                verticalMargin,
                measuredWidth - horizontalMargin,
                measuredHeight - verticalMargin * 2
            ),
            0f, 360f, false, eraser!!
        )
    }

    fun init(borderColor: Int, horizontalMargin: Float, verticalMargin: Float) {
        this.horizontalMargin = horizontalMargin
        this.verticalMargin = verticalMargin
        this.setLayerType(LAYER_TYPE_SOFTWARE, null)

        transparentBackground = Paint()
        transparentBackground!!.color = Color.BLACK
        transparentBackground!!.alpha = 100
        transparentBackground!!.style = Paint.Style.FILL
        borderPaint = Paint()
        borderPaint!!.color = borderColor
        borderPaint!!.strokeWidth = 15f
        borderPaint!!.style = Paint.Style.STROKE
        eraser = Paint()
        eraser!!.isAntiAlias = false
       // eraser!!.color = Color.CYAN
       eraser!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        eraser!!.style = Paint.Style.FILL
    }
}