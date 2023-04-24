package com.xueng.driver.app

import com.xueng.driver.app.databinding.ActivityDemoLayoutBinding

class MyDemoActivity : DriverToolbarActivity<ActivityDemoLayoutBinding>(R.layout.activity_demo_layout) {

    @BundleField
    var param:String=""


    override fun initContent() {

    }

    override fun getLayoutImmerSion(): Int = LINEAR
}