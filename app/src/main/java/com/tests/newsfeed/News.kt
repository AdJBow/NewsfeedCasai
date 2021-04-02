package com.tests.newsfeed

import Adapter.ViewHolder.ListSourceAdapter
import Interface.NewsService
import Model.WebSite
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import io.paperdb.Paper
import common.common
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_news.*
import retrofit2.Call
import retrofit2.Response


class News : AppCompatActivity() {
    lateinit var layoutManager:LinearLayoutManager
    lateinit var nService:NewsService
    lateinit var adapter: ListSourceAdapter
    lateinit var dialog: AlertDialog
    val FINE_LOCATION_RC = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        checkForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", FINE_LOCATION_RC)
        //Init cacheDB
        Paper.init(this)
        //Init service
        nService = common.newsService
        //Init view
        swipe_to_refresh.setOnRefreshListener {
            loadWebSiteSource(true)
        }

        recyclerView1.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        recyclerView1.layoutManager = layoutManager
        dialog = SpotsDialog.Builder().setContext(this).build()
        loadWebSiteSource(false)

    }

    private fun loadWebSiteSource(isRefresh: Boolean) {

        if(!isRefresh){
            val cache = Paper.book().read<String>("cache")
            if(cache != null && !cache.isBlank() && cache != "null"){
                //Read cache
                val webSite = Gson().fromJson<WebSite>(cache,WebSite::class.java)
                adapter = ListSourceAdapter(baseContext, webSite)
                adapter.notifyDataSetChanged()
                recyclerView1.adapter = adapter
            }
            else{
                //load Website and write cache
                dialog.show()
                //new data
                nService.sources.enqueue(object: retrofit2.Callback<WebSite>{
                    override fun onResponse(call: Call<WebSite>, response: Response<WebSite>) {
                       adapter = ListSourceAdapter(baseContext, response!!.body()!!)
                        adapter.notifyDataSetChanged()
                        recyclerView1.adapter = adapter
                        //save cache
                        Paper.book().write("cache",Gson().toJson(response!!.body()!!))

                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<WebSite>, t: Throwable) {
                        Toast.makeText(baseContext, "Failed",Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
        else{
            swipe_to_refresh.isRefreshing=true
            //fetch new data
            nService.sources.enqueue(object: retrofit2.Callback<WebSite>{
                override fun onResponse(call: Call<WebSite>, response: Response<WebSite>) {
                    adapter = ListSourceAdapter(baseContext, response!!.body()!!)
                    adapter.notifyDataSetChanged()
                    recyclerView1.adapter = adapter
                    //save cache
                    Paper.book().write("cache",Gson().toJson(response!!.body()!!))

                    swipe_to_refresh.isRefreshing = false
                }

                override fun onFailure(call: Call<WebSite>, t: Throwable) {
                    Toast.makeText(baseContext, "Failed",Toast.LENGTH_SHORT).show()
                }

            })
        }

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