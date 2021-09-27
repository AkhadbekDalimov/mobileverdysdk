package uz.click.myverdisdk.core

import androidx.annotation.Keep
import uz.click.myverdisdk.core.callbacks.VerdimUserListener
import uz.click.myverdisdk.impl.MainDialogFragment

/**
 * @author Azamat on 27/09/21
 * */
object VerdimUser {

    private val TAG_BOTTOM_SHEET = MainDialogFragment::class.java.name
    private lateinit var supportFragmentManager: androidx.fragment.app.FragmentManager

    @[JvmStatic Keep]
    fun init(
        supportFragmentManager: androidx.fragment.app.FragmentManager,
        config: VerdimUserConfig,
        listener: VerdimUserListener
    ) {
        VerdimUser.supportFragmentManager = supportFragmentManager
        if (findDialog(supportFragmentManager) == null) {
            val dialog = MainDialogFragment.newInstance(config)
            dialog.setListener(listener)
            dialog.show(supportFragmentManager, TAG_BOTTOM_SHEET)
        }
    }

    @[JvmStatic Keep]
    fun dismiss() {
        findDialog(supportFragmentManager)?.dismiss()
    }

    private fun findDialog(supportFragmentManager: androidx.fragment.app.FragmentManager) =
        supportFragmentManager.findFragmentByTag(TAG_BOTTOM_SHEET) as MainDialogFragment?


}