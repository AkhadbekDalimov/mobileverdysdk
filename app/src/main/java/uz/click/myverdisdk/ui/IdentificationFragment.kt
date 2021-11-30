package uz.click.myverdisdk.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import uz.click.myverdisdk.databinding.FragmentIdentificationBinding
import uz.click.myverdisdk.ui.adapter.ResultsViewPagerAdapter
import uz.click.myverdisdk.ui.adapter.StepsViewPagerAdapter

class IdentificationFragment : Fragment() {

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
    }

    override fun onResume() {
        val resultsViewPagerAdapter = ResultsViewPagerAdapter(childFragmentManager, lifecycle)
        binding.vpResults.adapter = resultsViewPagerAdapter
        TabLayoutMediator(binding.tlResults, binding.vpResults) { tab, position ->
            tab.text = when (position) {
                0 -> "Liveness"
                1 -> "Person"
                else -> "Address"
            }
        }.attach()
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() = IdentificationFragment()
    }
}