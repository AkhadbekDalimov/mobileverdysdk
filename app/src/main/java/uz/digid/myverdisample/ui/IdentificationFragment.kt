package uz.digid.myverdisample.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import uz.digid.myverdisample.databinding.FragmentIdentificationBinding
import uz.digid.myverdisample.ui.adapter.ResultsViewPagerAdapter
import uz.digid.myverdisample.utils.AppPreferences

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

        binding.btnLogout.setOnClickListener {
            AppPreferences.scannerSerialNumber = ""
            startActivity(
                Intent.makeRestartActivityTask(
                    requireActivity().intent.component
                )
            )
        }

        binding.btnRepeat.setOnClickListener {
            startActivity(
                Intent.makeRestartActivityTask(
                    requireActivity().intent.component
                )
            )
        }
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