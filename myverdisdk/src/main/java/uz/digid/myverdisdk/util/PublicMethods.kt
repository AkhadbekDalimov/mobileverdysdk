package uz.digid.myverdisdk.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

object PublicMethods {

    private const val PERMISSION_REQUESTS = 1
    private const val STATE_SELECTED_MODEL = "selected_model"
    private const val STATE_LENS_FACING = "lens_facing"

    fun isPermissionGranted(
        context: Context,
        permission: String?
    ): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission!!)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("Permission", " granted: $permission")
            return true
        }
        Log.i("Permission", " NOT granted: $permission")
        return false
    }

     fun requiredPermissions(activity: Activity): Array<String?> {
        try {
            val info = activity.packageManager
                .getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            return if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
           return arrayOfNulls(0)
        }
    }

     fun allPermissionsGranted(activity: Activity): Boolean {
        for (permission in requiredPermissions(activity)) {
            if (!isPermissionGranted(activity, permission)) {
                return false
            }
        }
        return true
    }

     fun runtimePermissions(activity: Activity) {

            val allNeededPermissions: MutableList<String?> = ArrayList()
            for (permission in requiredPermissions(activity)) {
                if (!isPermissionGranted(activity, permission)) {
                    allNeededPermissions.add(permission)
                }
            }
            if (allNeededPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    allNeededPermissions.toTypedArray(),
                    PERMISSION_REQUESTS
                )
            }
        }

}