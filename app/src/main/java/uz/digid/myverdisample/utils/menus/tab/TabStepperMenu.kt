
package uz.digid.myverdisample.utils.menus.tab

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import uz.digid.myverdisample.R
import uz.digid.myverdisample.utils.menus.base.StepperMenu
import uz.digid.myverdisample.utils.menus.base.StepperMenuItem
import kotlin.math.max

/**
 * Menu showing steps in tab mode.
 */
class TabStepperMenu(
    context: Context,
    override var widgetColor: Int,
    override var iconSizeInPX: Int,
    override var textAppearance: Int,
    override var textColor: Int,
    override var textSizeInPX: Int?
) : StepperMenu(context, widgetColor, iconSizeInPX, textAppearance, textColor, textSizeInPX) {

    override var currentStep: Int = 0

    override val menuItems: List<StepperMenuItem> get() = _menuItems

    private val _menuItems: ArrayList<TabStepperMenuItem> = arrayListOf()

    override fun updateUI() {
        _menuItems.forEachIndexed { index, item ->
            val labelView = item.labelView.apply {
                setTextAppearance(textAppearance)
                setTextColor(textColor)
                textSizeInPX?.let { setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat()) }
            }
            val iconView = item.iconView.apply {
                layoutParams = LayoutParams(iconSizeInPX, iconSizeInPX).apply {
                    startToStart = labelView.id
                    endToEnd = labelView.id
                    topToTop = this@TabStepperMenu.id
                }
            }
            val connectorView = item.connectorView?.apply {
                setBackgroundColor(widgetColor)
            }

            when {
                index == currentStep -> {
                    iconView.findViewById<AppCompatImageView>(R.id.icon_outer).run {
                        alpha = 1F
                        setImageDrawable(ColorDrawable(Color.WHITE))
                        imageTintList = ColorStateList.valueOf(Color.WHITE)
                    }
                    iconView.findViewById<AppCompatImageView>(R.id.icon_inner).run {
                        isVisible = true
                        setImageResource(R.drawable.bg_circle)
                        imageTintList = ColorStateList.valueOf(widgetColor)
                    }
                    labelView.alpha = 1F
                    connectorView?.alpha = 1F
                }
                index < currentStep -> {
                    iconView.findViewById<AppCompatImageView>(R.id.icon_outer).run {
                        alpha = 1F
                        setImageDrawable(ColorDrawable(widgetColor))
                        imageTintList = ColorStateList.valueOf(widgetColor)
                    }
                    iconView.findViewById<AppCompatImageView>(R.id.icon_inner).run {
                        isVisible = true
                        setImageResource(R.drawable.ic_check_mini)
                        imageTintList = ColorStateList.valueOf(Color.WHITE)
                    }
                    labelView.alpha = 1F
                    connectorView?.alpha = 1F
                }
                index > currentStep -> {
                    iconView.findViewById<AppCompatImageView>(R.id.icon_outer).run {
                        alpha = 0.4F
                        setImageResource(R.drawable.bg_circle_outline)
                        imageTintList = ColorStateList.valueOf(widgetColor)
                    }
                    iconView.findViewById<AppCompatImageView>(R.id.icon_inner).isVisible = false
                    labelView.alpha = 0.45F
                    connectorView?.alpha = 0.25F
                }
            }
        }
    }

    private fun addItemView(
        groupId: Int,
        itemId: Int,
        order: Int,
        title: CharSequence?
    ): TabStepperMenuItem {
        val iconView = LayoutInflater.from(context).inflate(
            R.layout.stepper_tab_item_icon,
            this,
            false
        ) as FrameLayout
        val labelView = LayoutInflater.from(context).inflate(
            R.layout.stepper_tab_item_label,
            this,
            false
        ) as TextView
        var connectorView: View? = null

        iconView.id = View.generateViewId()
        labelView.id = View.generateViewId()

        addView(iconView)
        addView(labelView)

        (labelView.layoutParams as LayoutParams).run {
            endToEnd = id
            topToBottom = iconView.id
        }

        if (_menuItems.isNotEmpty()) {
            val lastIconView = _menuItems.last().iconView
            val lastLabelView = _menuItems.last().labelView

            (lastLabelView.layoutParams as LayoutParams).run {
                endToStart = labelView.id
                endToEnd = -1
            }

            (labelView.layoutParams as LayoutParams).startToEnd = lastLabelView.id

            connectorView = View(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(0, 3 * resources.displayMetrics.density.toInt())
            }

            addView(connectorView)

            (connectorView.layoutParams as LayoutParams).run {
                topToTop = iconView.id
                bottomToBottom = iconView.id
                startToEnd = lastIconView.id
                endToStart = iconView.id
            }
        } else {
            (labelView.layoutParams as LayoutParams).startToStart = id
        }

        iconView.layoutParams = LayoutParams(iconSizeInPX, iconSizeInPX).apply {
            startToStart = labelView.id
            endToEnd = labelView.id
            topToTop = id
        }

        labelView.text = title
        labelView.measure(0, 0)

        val maxWidth = max(
            _menuItems.maxByOrNull { it.labelView.measuredWidth }?.labelView?.measuredWidth ?: 0,
            labelView.measuredWidth
        )

        _menuItems.forEach {
            it.labelView.layoutParams.width = maxWidth
        }

        labelView.run {
            TextViewCompat.setTextAppearance(this, textAppearance)
            setTextColor(textColor)
            textSizeInPX?.let { setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat()) }
            layoutParams.width = maxWidth
        }

        return TabStepperMenuItem(
            context,
            itemId,
            groupId,
            order,
            iconView,
            labelView,
            connectorView
        )
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
    ): MenuItem = addItemView(groupId, itemId, order, title).apply {
        _menuItems.add(this)
        _menuItems.sortBy { it.order }
        updateUI()
    }
}
