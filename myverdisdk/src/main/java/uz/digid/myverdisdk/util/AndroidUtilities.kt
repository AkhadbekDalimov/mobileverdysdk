package uz.digid.myverdisdk.util

import android.os.Handler

class AndroidUtilities(private val handler: Handler) {

    fun runOnUIThread(runnable: Runnable) {
        runOnUIThread(runnable, 0)
    }

    fun runOnUIThread(runnable: Runnable, delay: Long) {
        if (delay == 0L) {
            handler.post(runnable)
        } else {
            handler.postDelayed(runnable, delay)
        }
    }
}