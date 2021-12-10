package uz.digid.myverdisample.ui.results

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import uz.digid.myverdisample.R
import uz.digid.myverdisdk.core.Verdi
import uz.digid.myverdisample.databinding.FragmentLivenessBinding
import uz.digid.myverdisdk.impl.nfc.ImageUtil
import java.math.RoundingMode

class LivenessFragment : Fragment() {

    private lateinit var binding: FragmentLivenessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLivenessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livenessScore =
            Verdi.result.livenessAnswer?.validateResponse?.livenessScore?.liveness ?: -1.0
        val similarityScore =
            Verdi.result.livenessAnswer?.validateResponse?.similarityScore?.similarity ?: -1.0
        val bitmapPassport = ImageUtil.convert(Verdi.result.modelPersonPhoto?.personPhoto)
        if (bitmapPassport != null) {
            binding.ivPassport.setImageBitmap(bitmapPassport)
        }
        val bitmapSelfie = ImageUtil.convert(Verdi.result.modelPersonPhoto?.additional)
        if (bitmapSelfie != null) {
            binding.ivSelfie.setImageBitmap(bitmapSelfie)
        }

        binding.tvLivenessLabel.setText(R.string.liveness)
        binding.tvSimilarityLabel.setText(R.string.similarity)
        binding.tvLivenessValue.text =
            (if (livenessScore != -1.0) {
                livenessScore.toBigDecimal().setScale(2, RoundingMode.UP)
            } else {
                -1
            }).toString()
        binding.tvSimilarityValue.text =
            (if (livenessScore != -1.0) {
                similarityScore.toBigDecimal().setScale(2, RoundingMode.UP)
            } else {
                -1
            }).toString()

        if (livenessScore > 0.79) {
            binding.ivLiveness
                .setImageResource(R.drawable.ic_check)
        }
        if (similarityScore > 0.46) {
            binding.ivSimilarity
                .setImageResource(R.drawable.ic_check)
        }
        if ((livenessScore > 0.79 && similarityScore > 0.46)) {
            binding.apply {
                binding.tvInfo.apply {
                    setText(R.string.register_success)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.lightBlue))
                }
            }
        } else {
            binding.apply {
                binding.tvInfo.apply {
                    setText(R.string.register_error)
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.design_default_color_error
                        )
                    )
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = LivenessFragment()
    }

}