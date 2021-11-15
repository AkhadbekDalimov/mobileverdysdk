package uz.click.myverdisdk.utils

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String, argument : String = "") {
    Log.d(this::class.java.name + "Tag", "$message:$argument")
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}
