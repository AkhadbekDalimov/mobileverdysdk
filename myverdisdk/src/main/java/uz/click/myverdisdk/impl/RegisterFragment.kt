package uz.click.myverdisdk.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.view.ContextThemeWrapper
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.VerdimUserConfig

class RegisterFragment : AppCompatDialogFragment() {
    private lateinit var config: VerdimUserConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        config = requireArguments().getSerializable(
            MainDialogFragment.VERDIM_USER_CONFIG
        ) as VerdimUserConfig

        when (config.themeMode) {
            ThemeOptions.LIGHT -> {
                setStyle(STYLE_NO_FRAME, R.style.cl_FullscreenDialogTheme)

            }
            ThemeOptions.NIGHT -> {
                setStyle(STYLE_NO_FRAME, R.style.cl_FullscreenDialogThemeDark)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return when (config.themeMode) {
            ThemeOptions.LIGHT -> {
                val contextWrapper = ContextThemeWrapper(activity, R.style.Theme_App_Light)

                inflater.cloneInContext(contextWrapper)
                    .inflate(R.layout.fragment_register, container, false)
            }
            ThemeOptions.NIGHT -> {
                val contextWrapper = ContextThemeWrapper(activity, R.style.Theme_App_Dark)

                inflater.cloneInContext(contextWrapper)
                    .inflate(R.layout.fragment_register, container, false)
            }
        }
    }

}