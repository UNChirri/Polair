package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    private lateinit var pagerAdapter: ViewPagerAdapter

    private lateinit var rightPagerButton: Button

    private lateinit var leftPagerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        rightPagerButton = findViewById(R.id.pager_two)
        leftPagerButton = findViewById(R.id.pager_one)
        leftPagerButton.isPressed = true
        viewPager = findViewById(R.id.splash_viewpager)
        pagerAdapter = ViewPagerAdapter(supportFragmentManager, populateDescriptionList())
        viewPager.adapter = pagerAdapter

        initListeners()

    }

    fun initListeners(){
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                if (position == 0){
                    leftPagerButton.isPressed = true
                    rightPagerButton.isPressed = false
                } else {
                    leftPagerButton.isPressed = false
                    rightPagerButton.isPressed = true
                }
            }

        })

//        splash_register_button.setOnClickListener {
//            val intent = Intent(this, MapsActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun  populateDescriptionList() : ArrayList<Fragment>{
        val mobilizeFragment = MobilizeFragment()
        val shareFragment = ShareFragment()
        val descriptions = ArrayList<Fragment>()
        descriptions.add(mobilizeFragment)
        descriptions.add(shareFragment)
        return descriptions
    }

    fun startNextActivity(view: View) {
        val intent = Intent(view.context, LoginActivity::class.java)
        startActivity(intent)
    }
}