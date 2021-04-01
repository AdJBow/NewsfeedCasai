package com.tests.newsfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



class News : AppCompatActivity() {
    val FINE_LOCATION_RC = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

    //private fun checkForPermission(permission : String, name: String, requestCode: Int)
}