package uz.click.myverdisdk.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import uz.click.myverdisdk.MainViewModel
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.callbacks.VerdiListener
import uz.click.myverdisdk.databinding.FragmentPolicyApprovalBinding
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.model.response.AppIdResponse
import uz.click.myverdisdk.utils.AppPreferences
import uz.click.myverdisdk.utils.hide
import uz.click.myverdisdk.utils.show
import uz.click.myverdisdk.utils.toast
import java.lang.Exception


class PolicyApprovalFragment : Fragment(), VerdiListener {

    private lateinit var binding: FragmentPolicyApprovalBinding
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPolicyApprovalBinding.inflate(inflater, container, false)

        binding.btnLogout.isVisible = AppPreferences.scannerSerialNumber.isNotEmpty()
        binding.btnLogout.setOnClickListener {
            AppPreferences.scannerSerialNumber = ""
            startActivity(
                Intent.makeRestartActivityTask(
                    requireActivity().intent.component
                )
            )
        }

        binding.btnAgree.setOnClickListener {
            if (AppPreferences.scannerSerialNumber.isNotEmpty())
                Verdi.openSelfieActivity(
                    requireActivity(), this@PolicyApprovalFragment,
                    AppPreferences.scannerSerialNumber
                )
            else
                viewModel.changeStep(1)
        }
        return binding.root
    }

    override fun onSuccess() {
        viewModel.changeStep(2)
    }

    override fun onError(exception: Exception) {
        toast(exception.message.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = PolicyApprovalFragment()
    }
}