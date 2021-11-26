package uz.click.myverdisdk.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import uz.click.myverdisdk.MainViewModel
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.core.callbacks.VerdiListener
import uz.click.myverdisdk.databinding.FragmentDocumentInputBinding
import uz.click.myverdisdk.util.DocumentInputType
import uz.click.myverdisdk.util.DocumentInputValidation
import uz.click.myverdisdk.utils.toast
import java.lang.Exception

class DocumentInputFragment : Fragment(), VerdiListener {
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
            viewModel.changeStep(2)
        }
    }

    override fun onSuccess() {
        toast("Scan Success", Verdi.verdiUser.toString())
        binding.etDocumentNumber.setText(Verdi.verdiUser.serialNumber)
        binding.etDateOfBirth.setText(Verdi.verdiUser.birthDate)
        binding.etDateOfExpiry.setText(Verdi.verdiUser.dateOfExpiry)
        binding.tvDocumentNumber.text = Verdi.verdiUser.serialNumber
        binding.tvDateOfBirth.text = Verdi.verdiUser.birthDate
        binding.tvDateOfExpiry.text = Verdi.verdiUser.dateOfExpiry
        binding.tvPINFL.text = Verdi.verdiUser.personalNumber
    }

    override fun onError(exception: Exception) {

    }

    companion object {

        @JvmStatic
        fun newInstance() = DocumentInputFragment()
    }
}