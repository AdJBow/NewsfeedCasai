package com.tests.newsfeed

import Adapter.ViewHolder.ListNewsAdapter
import Interface.NewsService
import android.os.Bundle
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import common.Common
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news.swipe_to_refresh
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import Model.News1

class ListNews : AppCompatActivity() {

    var source=""
    var webHotUrl:String?=""

    lateinit var dialog: AlertDialog
    lateinit var nService:NewsService
    lateinit var adapter:ListNewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        //init view
        nService = Common.newsService

        dialog = SpotsDialog.Builder().setContext(this).build()

        swipe_to_refresh.setOnRefreshListener { loadNews(source,true) }

        diagonalLayout.setOnClickListener{
            //implement soon

        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager = LinearLayoutManager(this)


        if(intent != null){
            source = intent.getStringExtra("source").toString()
            if(!source.isEmpty()){
                loadNews(source,false)
            }
        }
    }

    private fun loadNews(source: String?, isRefreshed: Boolean) {
        if(isRefreshed){
            dialog.show()
            nService.getNewsFromSource(Common.getNewsAPI(source!!)).enqueue(object: Callback<News1> {
                override fun onFailure(call: Call<News1>?, t: Throwable) {
                }

                override fun onResponse(call: Call<News1>?, response: Response<News1>?) {
                    swipe_to_refresh.isRefreshing = true
                    Picasso.with(baseContext).load(response!!.body()!!.articles!![0].urlToImage)
                        .into(top_image)
                    top_title.text = response!!.body()!!.articles!![0].title
                    top_author.text = response!!.body()!!.articles!![0].author

                    webHotUrl = response!!.body()!!.articles!![0].url

                    //Load all remaining articles
                    val removeFirstItem = response!!.body()!!.articles
                    //remove first
                    removeFirstItem!!.removeAt(0)

                    adapter = ListNewsAdapter(removeFirstItem!!, baseContext)
                    adapter.notifyDataSetChanged()
                    list_news.adapter = adapter
                }

            })
        }
        else{
            swipe_to_refresh.isRefreshing = true
            nService.getNewsFromSource(Common.getNewsAPI(source!!)).enqueue(object: Callback<News1> {
                override fun onFailure(call: Call<News1>?, t: Throwable) {
                }

                override fun onResponse(call: Call<News1>?, response: Response<News1>?) {
                    swipe_to_refresh.isRefreshing = false
                    Picasso.with(baseContext).load(response!!.body()!!.articles!![0].urlToImage)
                        .into(top_image)
                    top_title.text = response!!.body()!!.articles!![0].title
                    top_author.text = response!!.body()!!.articles!![0].author

                    webHotUrl = response!!.body()!!.articles!![0].url

                    //Load all remaining articles
                    val removeFirstItem = response!!.body()!!.articles
                    //remove first
                    removeFirstItem!!.removeAt(0)

                    adapter = ListNewsAdapter(removeFirstItem!!, baseContext)
                    adapter.notifyDataSetChanged()
                    list_news.adapter = adapter
                }

            })
        }
    }
}

