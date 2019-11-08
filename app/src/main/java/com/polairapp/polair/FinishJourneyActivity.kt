package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_finish_journey.*

class FinishJourneyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_journey)
        setViewPagerAdapter()
        initWidgetsListeners()
    }

    private fun initWidgetsListeners() {
        viewPagerFinishJourney.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                if (position == 0){
                    leftButton.isPressed = true
                    rightButton.isPressed = false
                } else {
                    leftButton.isPressed = false
                    rightButton.isPressed = true
                }
            }

        })

        imvWhiteBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setViewPagerAdapter() {
        val listOfFragments = arrayListOf(CloudFragment(), BikersFragment())
        viewPagerFinishJourney.adapter = ViewPagerAdapter(supportFragmentManager, listOfFragments)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MapsActivity::class.java))
    }
}