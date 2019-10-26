package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_predict.*

class PredictActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)
        initButtonsListeners()
    }

    private fun initButtonsListeners() {
        btn_back.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
}