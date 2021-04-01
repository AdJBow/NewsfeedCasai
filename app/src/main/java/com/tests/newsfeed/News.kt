package com.tests.newsfeed

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class News : AppCompatActivity() {
    val FINE_LOCATION_RC = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        checkForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", FINE_LOCATION_RC)
    }

    private fun checkForPermission(permission : String, name: String, requestCode: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }

        when(requestCode){
            FINE_LOCATION_RC -> innerCheck("location")

        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Grant"){ dialog,which ->
                ActivityCompat.requestPermissions(this@News, arrayOf(permission), requestCode)

            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}