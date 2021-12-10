package uz.digid.myverdisample.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.digid.myverdisample.ui.DocumentInputFragment
import uz.digid.myverdisample.ui.IdentificationFragment
import uz.digid.myverdisample.ui.PolicyApprovalFragment

class StepsViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PolicyApprovalFragment.newInstance()
            1 -> uz.digid.myverdisample.ui.DocumentInputFragment.newInstance()
            2 -> IdentificationFragment.newInstance()
            else -> PolicyApprovalFragment.newInstance()
        }
    }

}