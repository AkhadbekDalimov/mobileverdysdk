package uz.click.myverdisdk.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.click.myverdisdk.ui.DocumentInputFragment
import uz.click.myverdisdk.ui.IdentificationFragment
import uz.click.myverdisdk.ui.PolicyApprovalFragment

class StepsViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PolicyApprovalFragment.newInstance()
            1 -> DocumentInputFragment.newInstance()
            2 -> IdentificationFragment.newInstance()
            else -> PolicyApprovalFragment.newInstance()
        }
    }

}