package uz.click.myverdisdk.ui.results

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.click.myverdisdk.R
import uz.click.myverdisdk.core.Verdi
import uz.click.myverdisdk.databinding.FragmentAddressBinding
import uz.click.myverdisdk.databinding.FragmentDocumentInputBinding
import uz.click.myverdisdk.databinding.ItemInfoBinding


class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addressPairList = Verdi.result.address?.getAddressPairList()
        addressPairList?.forEach {
            val itemInfoBinding = ItemInfoBinding.inflate(layoutInflater, binding.root, false)
            itemInfoBinding.tvLabel.text = it.first
            itemInfoBinding.tvValue.text = it.second
            binding.llAddressInfo.addView(itemInfoBinding.root)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddressFragment()
    }
}