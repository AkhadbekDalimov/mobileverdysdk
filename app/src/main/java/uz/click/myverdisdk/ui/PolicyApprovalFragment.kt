package uz.click.myverdisdk.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import uz.click.myverdisdk.MainViewModel
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.databinding.FragmentPolicyApprovalBinding
import uz.click.myverdisdk.model.response.AppIdResponse
import uz.click.myverdisdk.utils.hide
import uz.click.myverdisdk.utils.show
import uz.click.myverdisdk.utils.toast


class PolicyApprovalFragment : Fragment() {
    private lateinit var binding: FragmentPolicyApprovalBinding
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPolicyApprovalBinding.inflate(inflater, container, false)
        binding.btnAgree.setOnClickListener {
            binding.pbLoading.show()
            VerdiUser.checkAppId(requireActivity(), object : ResponseListener<AppIdResponse> {
                override fun onFailure(e: Exception) {
                    binding.pbLoading.hide()
                    toast(e.message.toString())
                }

                override fun onSuccess(response: AppIdResponse) {
                    binding.pbLoading.hide()
                    if (response.code == 0) {
                        viewModel.changeStep(1)
                    } else
                        toast(response.message)
                }
            })
        }
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = PolicyApprovalFragment()
    }
}