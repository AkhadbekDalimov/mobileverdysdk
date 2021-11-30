package uz.click.myverdisdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import uz.click.myverdisdk.databinding.ActivityMainBinding
import uz.click.myverdisdk.ui.adapter.StepsViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels<MainViewModel>()

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