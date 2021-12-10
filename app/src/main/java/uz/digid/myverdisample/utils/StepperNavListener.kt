
package uz.digid.myverdisample.utils

/**
 * Interface for getting callbacks from stepper navigation.
 */
interface StepperNavListener {

    /**
     * Callback for when the current step has been changed.
     *
     * @param step the new (0-indexed) step.
     */
    fun onStepChanged(step: Int)

    /**
     * Callback for when the stepper has reached the end of the steps.
     */
    fun onCompleted()
}
