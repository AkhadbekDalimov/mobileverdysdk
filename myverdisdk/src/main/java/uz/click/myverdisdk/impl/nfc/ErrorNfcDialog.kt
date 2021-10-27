package uz.click.myverdisdk.impl.nfc

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import uz.click.myverdisdk.R

class ErrorNfcDialog(context: Context) : AlertDialog(context, false, null) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_nfc_error)
        setTitle(R.string.nfc)

        findViewById<TextView>(R.id.tvMessage)?.setText(R.string.error_nfc)

//        buttonRetry.setOnClickListener {
        //todo nfc
//        }
    }
}