package uz.click.myverdisdk.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.click.myverdisdk.ui.results.AddressFragment
import uz.click.myverdisdk.ui.results.LivenessFragment
import uz.click.myverdisdk.ui.results.PersonFragment

class ResultsViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LivenessFragment.newInstance()
            1 -> PersonFragment.newInstance()
            2 -> AddressFragment.newInstance()
            else -> LivenessFragment.newInstance()
        }
    }

}