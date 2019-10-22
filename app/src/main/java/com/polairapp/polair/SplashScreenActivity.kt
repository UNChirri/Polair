package com.polairapp.polair

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    private lateinit var pagerAdapter: SplashViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        viewPager = findViewById(R.id.splash_viewpager)
        pagerAdapter = SplashViewPagerAdapter(supportFragmentManager, populateDescriptionList())
        viewPager.adapter = pagerAdapter
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