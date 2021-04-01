package Adapter.ViewHolder

import Interface.ItemClickListener
import Model.WebSite
import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tests.newsfeed.R
import kotlinx.android.synthetic.main.news_source.view.*
import java.util.*

class ListSourceAdapter (private val context: Context, private val webSite:WebSite):
    RecyclerView.Adapter<ListSourceViewerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSourceViewerHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val itemView = inflater.inflate(R.layout.news_source,parent,false)
        return ListSourceViewerHolder(itemView)
    }

    override fun getItemCount(): Int {
        return webSite.sources!!.size
    }

    override fun onBindViewHolder(holder: ListSourceViewerHolder, position: Int) {
        holder.source_title.text = webSite.sources!![position].name
        holder.setItemClickListener( object: ItemClickListener{

            override fun Onclick(View: View, position: Int) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }
        })
    }

}