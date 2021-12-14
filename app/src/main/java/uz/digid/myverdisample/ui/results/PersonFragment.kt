package uz.digid.myverdisample.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.digid.myverdisample.databinding.FragmentPersonBinding
import uz.digid.myverdisample.databinding.ItemInfoBinding
import uz.digid.myverdisdk.core.Verdi

class PersonFragment : Fragment() {

    private lateinit var binding: FragmentPersonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val personInfoPairList = Verdi.finalResult.personPairList

        personInfoPairList?.forEach {
            val itemInfoBinding = ItemInfoBinding.inflate(layoutInflater, binding.root, false)
            itemInfoBinding.tvLabel.text = it.first
            itemInfoBinding.tvValue.text = it.second
            binding.llPersonInfo.addView(itemInfoBinding.root)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PersonFragment()
    }
}