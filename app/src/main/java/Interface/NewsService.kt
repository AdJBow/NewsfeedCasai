package Interface

import Model.News1
import retrofit2.Call
import retrofit2.http.GET
import Model.WebSite
import com.tests.newsfeed.News
import retrofit2.http.Url

interface NewsService {
    @get:GET("v2/sources?apiKey=API_KEY")
    val sources:Call<WebSite>

    @GET
    fun getNewsFromSource(@Url url:String): Call<News1>
}