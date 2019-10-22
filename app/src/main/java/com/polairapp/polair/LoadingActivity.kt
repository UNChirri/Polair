package com.polairapp.polair

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class LoadingActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_loading)
        var imageView : ImageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.loading_bar).into(imageView);
    }
}