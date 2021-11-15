package uz.click.myverdisdk.utils.menus.base

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.ActionProvider
import android.view.ContextMenu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View

/**
 * Menu Item for managing interaction of menu items in [StepperMenu].
 */
abstract class StepperMenuItem(
    private val id: Int,
    private val groupId: Int = 0,
    private val order: Int = 0
) : MenuItem {

    /**
     * Function to be invoked when the item is clicked.
     */
    protected var onClickListener: (MenuItem) -> Unit = {}

    /**
     * Action is not supported.
     */
    override fun expandActionView(): Boolean = throw UnsupportedOperationException(
        "Action is not supported."
    )

    /**
     * Menu Item doesn't have sub menus.
     */
    override fun hasSubMenu(): Boolean = false

    /**
     * Menu Info is not supported.
     */
    override fun getMenuInfo(): ContextMenu.ContextMenuInfo = throw UnsupportedOperationException(
        "Menu Info is not supported."
    )

    /**
     * Get the [id] of the Menu Item.
     */
    override fun getItemId(): Int = id

    /**
     * Shortcut is not supported.
     */
    override fun getAlphabeticShortcut(): Char = throw UnsupportedOperationException(
        "Shortcut is not supported."
    )

    /**
     * Do nothing.
     */
    override fun setEnabled(enabled: Boolean): MenuItem = this

    /**
     * Do nothing.
     */
    override fun setChecked(checked: Boolean): MenuItem = this

    /**
     * Action is not supported.
     */
    override fun getActionView(): View = throw UnsupportedOperationException(
        "Action is not supported."
    )

    /**
     * Get the [order].
     */
    override fun getOrder(): Int = order

    /**
     * Do nothing.
     */
    override fun setOnActionExpandListener(listener: MenuItem.OnActionExpandListener?): MenuItem =
        this

    /**
     * Intent is not supported.
     */
    override fun getIntent(): Intent = throw UnsupportedOperationException(
        "Intent is not supported."
    )

    /**
     * Menu Item is always visible.
     */
    override fun setVisible(visible: Boolean): MenuItem = this

    /**
     * Menu Item is always enabled.
     */
    override fun isEnabled(): Boolean = true

    /**
     * Menu Item is never checkable.
     */
    override fun isCheckable(): Boolean = false

    /**
     * Action is not supported.
     */
    override fun setShowAsAction(actionEnum: Int) = throw UnsupportedOperationException(
        "Action is not supported."
    )

    /**
     * Gets the [groupId].
     */
    override fun getGroupId(): Int = groupId

    /**
     * Do nothing.
     */
    override fun setActionProvider(actionProvider: ActionProvider?): MenuItem = this

    /**
     * Do nothing.
     */
    override fun setTitleCondensed(title: CharSequence?): MenuItem = this

    /**
     * Shortcut is not supported.
     */
    override fun getNumericShortcut(): Char = throw UnsupportedOperationException(
        "Shortcut is not supported."
    )

    /**
     * Do nothing.
     */
    override fun isActionViewExpanded(): Boolean = false

    /**
     * Do nothing.
     */
    override fun collapseActionView(): Boolean = false

    /**
     * Menu Item is always visible.
     */
    override fun isVisible(): Boolean = true

    /**
     * Do nothing.
     */
    override fun setNumericShortcut(numericChar: Char): MenuItem = this

    /**
     * Do nothing.
     */
    override fun setActionView(view: View?): MenuItem = this

    /**
     * Do nothing.
     */
    override fun setActionView(resId: Int): MenuItem = this

    /**
     * Do nothing.
     */
    override fun setAlphabeticShortcut(alphaChar: Char): MenuItem = this

    /**
     * Icon is not supported.
     */
    override fun setIcon(icon: Drawable?): MenuItem = throw UnsupportedOperationException(
        "Icon is not supported."
    )

    /**
     * Do nothing.
     */
    override fun setIcon(iconRes: Int): MenuItem = this

    /**
     * Menu Item is never checked.
     */
    override fun isChecked(): Boolean = false

    /**
     * Do nothing.
     */
    override fun setIntent(intent: Intent?): MenuItem = this

    /**
     * Do nothing.
     */
    override fun setShortcut(
        numericChar: Char,
        alphaChar: Char
    ): MenuItem = this

    /**
     * Icon is not supported.
     */
    override fun getIcon(): Drawable = throw UnsupportedOperationException(
        "Icon is not supported."
    )

    /**
     * Do nothing.
     */
    override fun setShowAsActionFlags(actionEnum: Int): MenuItem = this

    /**
     * Assign a lambda function to be run when the item is clicked..
     */
    override fun setOnMenuItemClickListener(menuItemClickListener: MenuItem.OnMenuItemClickListener?): MenuItem {
        onClickListener = {
            menuItemClickListener?.onMenuItemClick(it)
        }
        return this
    }

    /**
     * Action Provider is not supported.
     */
    override fun getActionProvider(): ActionProvider = throw UnsupportedOperationException(
        "Action Provider is not supported."
    )

    /**
     * Do nothing.
     */
    override fun setCheckable(checkable: Boolean): MenuItem = this

    /**
     * Sub Menu is not supported.
     */
    override fun getSubMenu(): SubMenu = throw UnsupportedOperationException(
        "Sub Menu is not supported."
    )
}
