package com.polairapp.polair

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlin.properties.Delegates

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    private lateinit var pagerAdapter: SplashViewPagerAdapter

    private lateinit var rightPagerButton: Button

    private lateinit var leftPagerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        rightPagerButton = findViewById(R.id.pager_two)
        leftPagerButton = findViewById(R.id.pager_one)
        leftPagerButton.isPressed = true
        viewPager = findViewById(R.id.splash_viewpager)
        pagerAdapter = SplashViewPagerAdapter(supportFragmentManager, populateDescriptionList())
        viewPager.adapter = pagerAdapter

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

    }

    private fun  populateDescriptionList() : ArrayList<Fragment>{
        val mobilizeFragment = MobilizeFragment()
        val shareFragment = ShareFragment()
        val descriptions = ArrayList<Fragment>()
        descriptions.add(mobilizeFragment)
        descriptions.add(shareFragment)
        return descriptions
    }
}