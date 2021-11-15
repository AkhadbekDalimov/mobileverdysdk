
package uz.click.myverdisdk.utils.menus.tab

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import uz.click.myverdisdk.utils.menus.base.StepperMenuItem

/**
 * Shared menu item for [TabStepperMenu] and [TabNumberedStepperMenu].
 *
 * @property context the context to get resources from.
 * @property iconView the view containing the item icon label.
 * @property labelView the view containing the item step label.
 * @property connectorView the view containing the item step connector.
 */
class TabStepperMenuItem(
    private val context: Context,
    id: Int,
    groupId: Int = 0,
    order: Int = 0,
    val iconView: FrameLayout,
    val labelView: TextView,
    val connectorView: View? = null
) : StepperMenuItem(id, groupId, order) {

    init {
        iconView.setOnClickListener {
            onClickListener.invoke(this)
        }
        labelView.setOnClickListener {
            onClickListener.invoke(this)
        }
    }

    /**
     * Change the label view text of the item to the [title].
     */
    override fun setTitle(title: CharSequence?): MenuItem {
        labelView.text = title
        return this
    }

    /**
     * Set the title of the Menu Item.
     */
    override fun setTitle(title: Int): MenuItem {
        setTitle(context.getString(title))
        return this
    }

    /**
     * Get the title from the label view.
     */
    override fun getTitle(): CharSequence = labelView.text

    /**
     * Get the text in the label view.
     */
    override fun getTitleCondensed(): CharSequence = labelView.text
}
