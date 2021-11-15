
package uz.click.myverdisdk.utils.menus.progress

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ProgressBar
import uz.click.myverdisdk.R
import uz.click.myverdisdk.utils.menus.base.StepperMenu
import uz.click.myverdisdk.utils.menus.base.StepperMenuItem

/**
 * Menu showing steps progress in a progress bar.
 */
class ProgressStepperMenu(
    context: Context,
    override var widgetColor: Int,
    override var iconSizeInPX: Int,
    override var textAppearance: Int,
    override var textColor: Int,
    override var textSizeInPX: Int?
) : StepperMenu(context, widgetColor, iconSizeInPX, textAppearance, textColor, textSizeInPX) {

    private var progressAnimator: ValueAnimator = ValueAnimator.ofFloat()

    private val progressBar = LayoutInflater.from(context)
        .inflate(R.layout.stepper_progress, this, false) as FrameLayout

    init {
        progressBar.id = View.generateViewId()

        (progressBar.layoutParams as LayoutParams).run {
            startToStart = id
            endToEnd = id
            topToTop = id
            bottomToBottom = id
        }

        addView(progressBar)
    }

    override var currentStep: Int = 0

    override val menuItems: List<StepperMenuItem> get() = _menuItems

    private val _menuItems: ArrayList<ProgressStepperMenuItem> = arrayListOf()

    override fun updateUI() {
        val percentCompleted = ((currentStep + 1F) / _menuItems.size) * 100
        progressBar.findViewById<ProgressBar>(R.id.progress_stepper).run {
            progressTintList = ColorStateList.valueOf(widgetColor)
            progressAnimator.cancel()
            progressAnimator =
                ValueAnimator.ofFloat(progress.toFloat(), percentCompleted).setDuration(200)
            progressAnimator.run {
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    progress = (it.animatedValue as Float).toInt()
                }
                start()
            }
        }
    }

    /**
     * Remove all menu items.
     */
    override fun clear() {
        _menuItems.clear()
        updateUI()
    }

    /**
     * Remove menu item with item id [id].
     */
    override fun removeItem(id: Int) {
        _menuItems.removeAll { it.itemId == id }
        updateUI()
    }

    /**
     * Remove menu items associated with [groupId].
     */
    override fun removeGroup(groupId: Int) {
        _menuItems.removeAll { groupId == it.groupId }
        updateUI()
    }

    /**
     * Add a new menu item.
     */
    override fun add(
        groupId: Int,
        itemId: Int,
        order: Int,
        title: CharSequence?
    ): MenuItem = ProgressStepperMenuItem(itemId).apply {
        _menuItems.add(this)
        _menuItems.sortBy { it.order }
        updateUI()
    }
}
