package uz.digid.myverdisample.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import uz.digid.myverdisample.databinding.FragmentDocumentInputBinding
import uz.digid.myverdisdk.core.Verdi
import uz.digid.myverdisdk.core.callbacks.VerdiListener
import uz.digid.myverdisdk.core.callbacks.VerdiRegisterListener
import uz.digid.myverdisdk.util.DocumentInputType
import uz.digid.myverdisdk.util.DocumentInputValidation
import uz.digid.myverdisample.utils.AppPreferences
import uz.digid.myverdisample.utils.toast
import java.lang.Exception

class DocumentInputFragment : Fragment(), VerdiListener, VerdiRegisterListener {
    private lateinit var binding: FragmentDocumentInputBinding
    private val viewModel by activityViewModels<uz.digid.myverdisample.MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDocumentInputBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnScanPassport.setOnClickListener {
            Verdi.openDocumentScanActivity(requireActivity(), this)
        }
        binding.btnScanId.setOnClickListener {
            Verdi.openDocumentScanActivity(requireActivity(), this, true)
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
            Verdi.proceedNfcAndSelfie(
                requireActivity(),
                viewModel.passportSeries,
                viewModel.dateOfBirth,
                viewModel.dateOfExpiry,
                this
            )
        }

        viewModel.passportSeries = binding.etDocumentNumber.text.toString()
        viewModel.dateOfBirth = binding.etDateOfBirth.text.toString()
        viewModel.dateOfExpiry = binding.etDateOfExpiry.text.toString()
        viewModel.checkNextButtonEnable()
    }

    override fun onSuccess() {
        toast("Scan Success", Verdi.user.toString())
        binding.etDocumentNumber.setText(Verdi.user.serialNumber)
        binding.etDateOfBirth.setText(Verdi.user.birthDate)
        binding.etDateOfExpiry.setText(Verdi.user.dateOfExpiry)
        binding.tvDocumentNumber.text = Verdi.user.serialNumber
        binding.tvDateOfBirth.text = Verdi.user.birthDate
        binding.tvDateOfExpiry.text = Verdi.user.dateOfExpiry
        binding.tvPINFL.text = Verdi.user.personalNumber
    }

    override fun onError(exception: Exception) {
        toast(exception.message.toString())
    }

    override fun onRegisterError(exception: Exception) {
        toast(exception.message.toString())
        viewModel.changeStep(2)
    }

    override fun onRegisterSuccess() {
        AppPreferences.scannerSerialNumber = Verdi.user.scannerSerial
        viewModel.changeStep(2)
    }

    companion object {
        @JvmStatic
        fun newInstance() = uz.digid.myverdisample.ui.DocumentInputFragment()
    }
}