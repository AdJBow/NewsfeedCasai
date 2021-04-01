package common

import Interface.NewsService
import Remote.RetrofitClient

object common{
    val BASE_URL = "https://newsapi.org/"
    val API_KEY = "756560bf64d049129b005ebda73d3391"

    val newsService:NewsService
        get()=RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)
}
