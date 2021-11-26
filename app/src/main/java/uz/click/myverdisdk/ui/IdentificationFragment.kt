package uz.click.myverdisdk.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.callbacks.VerdiNfcListener
import uz.click.myverdisdk.databinding.FragmentIdentificationBinding
import uz.click.myverdisdk.utils.hide
import uz.click.myverdisdk.utils.toast

import android.content.DialogInterface

import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import uz.click.myverdisdk.core.callbacks.VerdiListener
import uz.click.myverdisdk.core.errors.NfcInvalidDataException
import uz.click.myverdisdk.model.request.RegistrationResponse
import uz.click.myverdisdk.utils.show


class IdentificationFragment : Fragment(),
    VerdiListener,
    VerdiNfcListener {

    private lateinit var binding: FragmentIdentificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIdentificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onNfcChecked(Verdi.isNfcAvailable, Verdi.isNfcEnabled)
        binding.btnNFCScan.setOnClickListener {
            Verdi.openNfcScanActivity(requireActivity(),this)
        }
        binding.btnTakeSelfie.setOnClickListener {
            Verdi.openSelfieActivity(requireActivity(), this)
        }

        binding.btnRegister.setOnClickListener {
            binding.pbLoading.show()
            Verdi.registerPerson(
                object : ResponseListener<RegistrationResponse> {
                    override fun onFailure(e: Exception) {
                        binding.pbLoading.hide()
                        toast(e.message.toString())
                    }

                    override fun onSuccess(response: RegistrationResponse) {
                        binding.pbLoading.hide()
                        toast(response.message)
                    }
                })
        }
    }

    override fun onSuccess() {
        toast("Selfie taken successfully")
        binding.ivSelfie.setImageBitmap(Verdi.verdiUser.imageFaceBase)
    }

    override fun onError(exception: Exception) {

    }

    override fun onNfcScanned() {
        toast("Nfc Successfully read!")
    }

    override fun onNfcError(exception: java.lang.Exception) {
        when(exception){
            is NfcInvalidDataException->{
                //TODO
            }
        }
    }


    private fun onNfcChecked(isNfcAvailable: Boolean, isNfcEnabled: Boolean) {
        Log.d("NfcCheckTag", "Available: $isNfcAvailable  Enabled : $isNfcEnabled" )
        if (!isNfcAvailable) {
            binding.btnNFCScan.hide()
            return
        }
        if (!isNfcEnabled) {
            val nfcAlertDialog = AlertDialog.Builder(requireContext())
            nfcAlertDialog.setTitle("NFC")
            nfcAlertDialog.setMessage("Your NFC is turned off, please turn it on.")
            nfcAlertDialog.setPositiveButton("Turn On",
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                    startActivity(intent)
                })
            nfcAlertDialog.setNegativeButton("Close",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            nfcAlertDialog.show()
        }
        binding.btnNFCScan.isVisible = isNfcEnabled

    }



    companion object {
        @JvmStatic
        fun newInstance() = IdentificationFragment()
    }
}