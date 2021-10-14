package uz.click.myverdisdk.impl

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.VerdiUserConfig
import uz.click.myverdisdk.core.callbacks.VerdiUserListener
import uz.click.myverdisdk.core.errors.ArgumentEmptyException

class MainDialogFragment : BottomSheetDialogFragment() {
    private val registerPage = RegisterFragment::class.java.name
    private var listener: VerdiUserListener? = null
    private lateinit var config: VerdiUserConfig
    companion object {
        private const val APP_NAME = "air.com.ssdsoftwaresolutions.clickuz"
        const val VERDIM_USER_CONFIG = "VERDIM_USER_CONFIG"
        const val REQUEST_ID = "REQUEST_ID"
        const val PAYMENT_RESULT = "PAYMENT_RESULT"
        const val PAYMENT_AMOUNT = "PAYMENT_AMOUNT"
        const val LOCALE = "LOCALE"
        const val THEME_MODE = "THEME_MODE"
        const val IS_CLICK_EVOLUTION_ENABLED = "IS_CLICK_EVOLUTION_ENABLED"


        fun newInstance(config: VerdiUserConfig?): MainDialogFragment {
            val bundle = Bundle()
            bundle.putSerializable(VERDIM_USER_CONFIG, config)
            val dialog = MainDialogFragment()
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            dismiss()
            return
        }
        if (arguments == null) throw ArgumentEmptyException()
        config = requireArguments().getSerializable(VERDIM_USER_CONFIG) as VerdiUserConfig
        when (config.themeMode) {
            ThemeOptions.LIGHT -> {
                setStyle(STYLE_NO_FRAME, R.style.cl_MainDialogTheme)
            }
            ThemeOptions.NIGHT -> {
                setStyle(STYLE_NO_FRAME, R.style.cl_MainDialogThemeDark)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return when (config.themeMode) {
            ThemeOptions.LIGHT -> {
                val contextWrapper = ContextThemeWrapper(activity, R.style.Theme_App_Light)

                inflater.cloneInContext(contextWrapper)
                    .inflate(R.layout.dialog_bottom_sheet, container, false)
            }
            ThemeOptions.NIGHT -> {
                val contextWrapper = ContextThemeWrapper(activity, R.style.Theme_App_Dark)

                inflater.cloneInContext(contextWrapper)
                    .inflate(R.layout.dialog_bottom_sheet, container, false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            (dialog as? BottomSheetDialog)?.also { dialog ->
                val bottomSheet =
                    dialog.findViewById<FrameLayout?>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isHideable = true
                    peekHeight = 0
                }
            }
        }
        view.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)

        val transaction = childFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putSerializable(VERDIM_USER_CONFIG, config)
        val payment = RegisterFragment()
        payment.arguments = bundle
        transaction.add(R.id.bottomSheetContainer, payment, registerPage)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
//        dialog?.setOnKeyListener { _, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                if (event.action != KeyEvent.ACTION_DOWN) {
//                    val option =
//                        childFragmentManager.findFragmentByTag(paymentOptions) as PaymentOptionListFragment?
//                    val scan = childFragmentManager.findFragmentByTag(scan) as ScanFragment?
//                    if (option != null) {
//                        if (option.isVisible) {
//                            childFragmentManager.popBackStack()
//                            showPaymentPage()
//                        }
//                    } else
//                        if (scan != null) {
//                            if (scan.isVisible) {
//                                childFragmentManager.popBackStack()
//                                showPaymentPage()
//                            }
//                        } else dismiss()
//                }
//
//                true
//            } else
//                false
//        }
    }


    private fun appInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = requireContext().packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }


    fun close() {
        dismiss()
    }

    private fun hideFragments() {
        val transaction = childFragmentManager.beginTransaction()
        for (fragment in childFragmentManager.fragments) {
            transaction.hide(fragment)
        }
        transaction.commitAllowingStateLoss()
    }

    fun setListener(listener: VerdiUserListener) {
        this.listener = listener
    }


}