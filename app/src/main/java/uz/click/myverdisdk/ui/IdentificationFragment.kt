package uz.click.myverdisdk.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.click.myverdisdk.core.VerdiUser
import uz.click.myverdisdk.core.callbacks.ResponseListener
import uz.click.myverdisdk.core.callbacks.VerdiNfcCheckListener
import uz.click.myverdisdk.core.callbacks.VerdiNfcListener
import uz.click.myverdisdk.core.errors.NFCNotAvailableException
import uz.click.myverdisdk.core.errors.NFCNotEnabledException
import uz.click.myverdisdk.databinding.FragmentIdentificationBinding
import uz.click.myverdisdk.utils.hide
import uz.click.myverdisdk.utils.toast
import android.os.Build

import android.content.DialogInterface

import android.R
import android.graphics.Bitmap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import uz.click.myverdisdk.core.callbacks.VerdiSelfieListener
import uz.click.myverdisdk.model.request.RegistrationResponse


class IdentificationFragment : Fragment(),
    VerdiNfcListener,
    VerdiNfcCheckListener {
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
        VerdiUser.checkNFCAvailability(requireContext(), this)

        binding.btnNFCScan.setOnClickListener {
            VerdiUser.openNfcScanActivity(requireActivity())
        }
        binding.btnTakeSelfie.setOnClickListener {
            VerdiUser.openSelfieActivity(requireActivity())
            VerdiUser.config.selfieListener = object : VerdiSelfieListener {
                override fun onSelfieSuccess(selfie: Bitmap) {
                    binding.ivSelfie.setImageBitmap(selfie)
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            VerdiUser.registerPerson(
                requireActivity(),
                object : ResponseListener<RegistrationResponse> {
                    override fun onFailure(e: Exception) {
                        toast(e.message.toString())
                    }

                    override fun onSuccess(response: RegistrationResponse) {
                        toast(response.toString())
                    }
                })
        }
    }

    override fun onNfcSuccess() {
        toast("Nfc Successfully read!")
    }

    override fun onNfcChecked(isNfcAvailable: Boolean, isNfcEnabled: Boolean) {
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


    }

    companion object {
        @JvmStatic
        fun newInstance() = IdentificationFragment()
    }
}