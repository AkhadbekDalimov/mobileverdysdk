package uz.digid.myverdisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import uz.digid.myverdisample.databinding.ActivityMainBinding
import uz.digid.myverdisample.ui.adapter.StepsViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private val viewModel : uz.digid.myverdisample.MainViewModel by viewModels<uz.digid.myverdisample.MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stepsViewPagerAdapter = StepsViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.vpSteps.adapter = stepsViewPagerAdapter
        binding.vpSteps.isUserInputEnabled = false
        viewModel.stepLiveData.observe(this, Observer {
            binding.vpSteps.currentItem = it
            binding.stepperNavigation.goToSelectedStep(it)
        })


    }
}