package com.example.frontend.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.frontend.R
import com.example.frontend.models.Movie

class MovieAdapter(
    private val context: Context,
    private val dataSource: List<Movie>
) : BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(p0: Int): Any {
        return dataSource[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_movie, p2, false)

        val idTextView = rowView.findViewById<TextView>(R.id.item_id)
        val titleTextView = rowView.findViewById<TextView>(R.id.item_title)
        val subtitleTextView = rowView.findViewById<TextView>(R.id.item_subtitle)

        val item = getItem(p0) as Movie

        idTextView.text = item.id.toString()
        titleTextView.text = item.name
        subtitleTextView.text = item.genre

        return rowView
    }
}
