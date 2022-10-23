package com.example.frontend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.models.Movie

class MovieDetailActivity: AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val id = intent.extras?.getString(EXTRA_ID )

        val idet = findViewById<EditText>(R.id.textView2)
        idet.setText(id.toString())
    }

    companion object {
        const val EXTRA_ID = "id"


        fun newIntent(context: Context, movie: Movie): Intent {
            val detailIntent = Intent(context, MovieDetailActivity::class.java)
            detailIntent.putExtra(EXTRA_ID, movie.id.toString())
            return detailIntent
        }
    }

}