package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class LoadingActivity : AppCompatActivity(){

    private var mDelayHandler: Handler? = null
    private val GIF_DELAY: Long = 5000 //5 seconds
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            startNextActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val imageView : ImageView = findViewById(R.id.air_gif)
        Glide.with(this).asGif().load(R.raw.loading_bar).into(imageView)
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, GIF_DELAY)
    }

    override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }


    private fun startNextActivity(){
        val intent = Intent(this, MapsActivity::class.java)
        finish()
        startActivity(intent)
    }
}