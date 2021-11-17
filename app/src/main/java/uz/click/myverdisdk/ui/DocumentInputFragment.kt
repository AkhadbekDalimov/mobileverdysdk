package uz.click.myverdisdk.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import uz.click.myverdisdk.MainViewModel
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.callbacks.VerdiScanListener
import uz.click.myverdisdk.databinding.FragmentDocumentInputBinding
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.util.DocumentInputType
import uz.click.myverdisdk.util.DocumentInputValidation
import uz.click.myverdisdk.utils.hide
import uz.click.myverdisdk.utils.show
import uz.click.myverdisdk.utils.toast
import java.time.LocalDateTime

class DocumentInputFragment : Fragment(), VerdiScanListener {
    private lateinit var binding: FragmentDocumentInputBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDocumentInputBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        VerdiUser.config.scanListener = this
        binding.btnScanPassport.setOnClickListener {
            VerdiUser.openDocumentScanActivity(requireActivity())
        }
        binding.btnScanQrId.setOnClickListener {
            VerdiUser.openDocumentScanActivity(requireActivity())
        }
        viewModel.nextButtonEnabled.observe(viewLifecycleOwner, Observer {
            binding.btnNext.isEnabled = it
        })
        binding.etDocumentNumber.doAfterTextChanged {
            viewModel.passportSeries = it.toString()
            viewModel.checkNextButtonEnable()
            binding.tlDocumentNumber.error =
                if (DocumentInputValidation.isInputValid(DocumentInputType.PASSPORT(it.toString())))
                    null else
                    "Wrong passport"
        }

        binding.etDateOfBirth.doAfterTextChanged {
            viewModel.dateOfBirth = it.toString()
            viewModel.checkNextButtonEnable()
            binding.tlDateOfBirth.error =
                if (DocumentInputValidation.isInputValid(DocumentInputType.BIRTHDAY(it.toString())))
                    null
                else "Invalid birth date"
        }

        binding.etDateOfExpiry.doAfterTextChanged {
            viewModel.dateOfExpiry = it.toString()
            viewModel.checkNextButtonEnable()
            binding.tlDateOfExpiry.error =
                if (DocumentInputValidation.isInputValid(DocumentInputType.EXPIRATION(it.toString())))
                    null else
                    "Invalid date"
        }

        binding.btnNext.setOnClickListener {
            viewModel.changeStep(2)
        }
    }

    override fun onScanSuccess() {
        toast("Scan Success", VerdiUser.config.toString())
        binding.etDocumentNumber.setText(VerdiUser.config.serialNumber)
        binding.etDateOfBirth.setText(VerdiUser.config.birthDate)
        binding.etDateOfExpiry.setText(VerdiUser.config.dateOfExpiry)
        binding.tvDocumentNumber.text = VerdiUser.config.serialNumber
        binding.tvDateOfBirth.text = VerdiUser.config.birthDate
        binding.tvDateOfExpiry.text = VerdiUser.config.dateOfExpiry
        binding.tvPINFL.text = VerdiUser.config.personalNumber
    }

    companion object {

        @JvmStatic
        fun newInstance() = DocumentInputFragment()
    }
}