package com.xueng.driver.app
import com.blankj.utilcode.util.LogUtils
import com.xueng.driver.app.databinding.ActivityMainBinding
import com.xueng.driver.router.DriverRouter
import com.xueng.driver.toast.toast

class MainActivity : DriverBaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun getTag(): String ="main"

    override fun initContent() {

         setDrakState(false)

         binding.tvClick.setOnClickListener {

               //普通跳转
//             DriverRouter.build(MyDemoActivity::class.java)
//                 .put("param", "param")
//                 .start()

              //startActivtyForResult
              DriverRouter.build(MyDemoActivity::class.java)
                 .put("param", "param")
                 .start(this,0x9){resultCode, data ->
                     LogUtils.e("$resultCode,------$data")
                 }

         }
    }



}