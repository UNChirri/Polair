package com.polairapp.polair

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class LoadingActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val imageView : ImageView = findViewById(R.id.air_gif);
        Glide.with(this).load(R.drawable.loading_bar).into(imageView);
    }
}