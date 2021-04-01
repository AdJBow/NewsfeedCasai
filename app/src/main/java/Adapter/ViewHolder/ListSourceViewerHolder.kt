package Adapter.ViewHolder

import Interface.ItemClickListener
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.news_source.view.*

class ListSourceViewerHolder(itemView:View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var itemClickListener: ItemClickListener
    var source_title = itemView.source_news_name
    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?) {
        itemClickListener.Onclick(v!!,adapterPosition)
    }
}