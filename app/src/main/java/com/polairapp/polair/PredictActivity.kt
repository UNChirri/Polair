package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_predict.*

class PredictActivity : AppCompatActivity() {

    var groupLogic : BooleanArray = booleanArrayOf(false, false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)
        initButtonsListeners()
    }

    private fun switchButtonsLogicOff (buttonIndex : Int) {
        for (i in groupLogic.indices) {
            if (i != buttonIndex) {
                groupLogic[i] = false
            }
        }
    }

    private fun initButtonsListeners() {
        btn_back.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }
        btn_pm25.setOnClickListener{
            groupLogic[0] = !groupLogic[0]
            if (groupLogic[0]){
                btn_pm25.setBackgroundResource(R.drawable.ic_pm_25_sel)
                btn_pm10.setBackgroundResource(R.drawable.ic_pm_10)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                btn_o3.setBackgroundResource(R.drawable.ic_o_3)
                btn_so2.setBackgroundResource(R.drawable.ic_so_2)
                btn_co.setBackgroundResource(R.drawable.ic_co)
                switchButtonsLogicOff(0)
            } else btn_pm25.setBackgroundResource(R.drawable.ic_pm_25)
        }
        btn_pm10.setOnClickListener {
            groupLogic[1] = !groupLogic[1]
            if (groupLogic[1]) {
                btn_pm10.setBackgroundResource(R.drawable.ic_pm_10_sel)
                btn_pm25.setBackgroundResource(R.drawable.ic_pm_25)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                btn_o3.setBackgroundResource(R.drawable.ic_o_3)
                btn_so2.setBackgroundResource(R.drawable.ic_so_2)
                btn_co.setBackgroundResource(R.drawable.ic_co)
                switchButtonsLogicOff(1)
            } else btn_pm10.setBackgroundResource(R.drawable.ic_pm_10)
        }
        btn_no2.setOnClickListener {
            groupLogic[2] = !groupLogic[2]
            if (groupLogic[2]) {
                btn_no2.setBackgroundResource(R.drawable.ic_no_2_sel)
                btn_pm10.setBackgroundResource(R.drawable.ic_pm_10)
                btn_pm25.setBackgroundResource(R.drawable.ic_pm_25)
                btn_o3.setBackgroundResource(R.drawable.ic_o_3)
                btn_so2.setBackgroundResource(R.drawable.ic_so_2)
                btn_co.setBackgroundResource(R.drawable.ic_co)
                switchButtonsLogicOff(2)
            } else btn_no2.setBackgroundResource(R.drawable.ic_no_2)

        }
        btn_o3.setOnClickListener {
            groupLogic[3] = !groupLogic[3]
            if (groupLogic[3]) {
                btn_o3.setBackgroundResource(R.drawable.ic_o_3_sel)
                btn_pm10.setBackgroundResource(R.drawable.ic_pm_10)
                btn_pm25.setBackgroundResource(R.drawable.ic_pm_25)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                btn_so2.setBackgroundResource(R.drawable.ic_so_2)
                btn_co.setBackgroundResource(R.drawable.ic_co)
                switchButtonsLogicOff(3)
            } else btn_o3.setBackgroundResource(R.drawable.ic_o_3)

        }
        btn_so2.setOnClickListener {
            groupLogic[4] = !groupLogic[4]
            if (groupLogic[4]) {
                btn_so2.setBackgroundResource(R.drawable.ic_so_2_sel)
                btn_pm10.setBackgroundResource(R.drawable.ic_pm_10)
                btn_pm25.setBackgroundResource(R.drawable.ic_pm_25)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                btn_o3.setBackgroundResource(R.drawable.ic_o_3)
                btn_co.setBackgroundResource(R.drawable.ic_co)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                switchButtonsLogicOff(4)
            } else btn_so2.setBackgroundResource(R.drawable.ic_so_2)

        }
        btn_co.setOnClickListener {
            groupLogic[5] = !groupLogic[5]
            if (groupLogic[5]) {
                btn_co.setBackgroundResource(R.drawable.ic_co_sel)
                btn_pm10.setBackgroundResource(R.drawable.ic_pm_10)
                btn_pm25.setBackgroundResource(R.drawable.ic_pm_25)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                btn_o3.setBackgroundResource(R.drawable.ic_o_3)
                btn_no2.setBackgroundResource(R.drawable.ic_no_2)
                btn_so2.setBackgroundResource(R.drawable.ic_so_2)
                switchButtonsLogicOff(5)
            } else btn_co.setBackgroundResource(R.drawable.ic_co)
        }
    }
}