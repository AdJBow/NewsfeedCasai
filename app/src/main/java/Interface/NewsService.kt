package Interface

import retrofit2.Call
import retrofit2.http.GET
import Model.WebSite

interface NewsService {
    @get:GET("v2/sources?apiKey=API_KEY")
    val sources:Call<WebSite>
}