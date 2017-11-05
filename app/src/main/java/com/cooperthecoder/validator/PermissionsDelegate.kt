package com.cooperthecoder.validator

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionsDelegate(private val activity: Activity) {

    fun hasCameraPermission(): Boolean {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
        )
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE
        )
    }

    fun resultGranted(requestCode: Int,
                      permissions: Array<String>,
                      grantResults: IntArray): Boolean {

        if (requestCode != REQUEST_CODE) {
            return false
        }

        if (grantResults.isEmpty()) {
            return false
        }
        if (permissions[0] != Manifest.permission.CAMERA) {
            return false
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        requestCameraPermission()
        return false
    }

    companion object {

        private val REQUEST_CODE = 10
    }
}
