# Driver驱动

此库主要是封装基类  
    1.可以通过TAG来关闭某一些Activity，比如支付流程，订单流程，这些流程在完成是要全部关闭，可以给这些activity设置相同的Tag  
    2.databing封装  
    3.沉浸式状态栏嵌入(immersionbar)集成  
    4.屏幕适配集成  
    5.DriverToolbarActivity 内部嵌入一个title  
    6.DriverActivityResult 让startActivityForResult实现解耦  
    7.DriverBundle 可以使用注解形式获取Bundle的值，无需intnet.get  
    8.内置toast，无需传入context，在任意地方可以直接toast  
    9.fragment封装DriverBaseFragment  DriverDialogFragment  
    10.fragment添加扩展，实现一个函数让viewpager2和fragment结合，viewpager2+fragment+BottomNavigationView结合  

## 安装


添加远程仓库根据创建项目的 Android Studio 版本有所不同
```
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
Android Studio Arctic Fox以上创建的项目 在项目根目录的 settings.gradle 添加仓库
```
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
然后在 module 的 build.gradle 添加依赖框架  
```
implementation 'com.github.liangjingkanji:Engine:0.0.72'

```
## 用法

   1.首先在gradle  android{}中添加：  
```
       buildFeatures {
        dataBinding = true
        viewBinding = true
    }
```

 2.application注册
```
  class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Driver.initialize(this)
    }
}

别放了manifest加上
android:name=".App"
```
3.开始使用

# 基类
```
class MyDemoActivity : DriverToolbarActivity<ActivityDemoLayoutBinding>(R.layout.activity_demo_layout) {
     
    override fun getTag(): String ="main"  //辅助  finishActivities("main") 可以把activty栈中设置为"main"的全部关闭 

    override fun initContent() {
         setDrakState(false) //设置状态栏是不是暗色
    }
    
    override fun getLayoutImmerSion(): Int = LINEAR  //open方法  设置titlebar的布局方式  LINEAR 和 FRAME  线性和帧布局 当沉浸式背景是图片时候就明白了
}
```

```
class MainActivity : DriverBaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initContent() {

    }
}
```
# 跳转

```
               //普通跳转
               DriverRouter.build(MyDemoActivity::class.java)
                .put("param", "param")
                 .start()

              //startActivtyForResult  实现解耦
              DriverRouter.build(MyDemoActivity::class.java)
                 .put("param", "param")
                 .start(this,0x9){resultCode, data ->
                     LogUtils.e("$resultCode,------$data")
                 }
```
  MyDemoActivity
```
   class MyDemoActivity : DriverToolbarActivity<ActivityDemoLayoutBinding>(R.layout.activity_demo_layout) {
 
   //使用注解形式 获取传值  变量名必须跟key的名一样
    @BundleField
    var param:String=""
 }
```


# fragment

```
  class DemoFragment : DriverBaseFragment<FragmentDemoLayoutBinding>(R.layout.fragment_demo_layout) {
    override fun initContent() {
       
    }
}
```
# fragment扩展 viewpage ，Navigation

```
     NavigationMediator(this, binding.mainBnv, binding.mainVp2, mFragments) { bnv, vp2 ->
            vp2.isUserInputEnabled=false
            bnv.itemIconTintList=null
        }.attach()
```

```
        binding.mvpCoupon.adapter=FragmentPagerAdapter(mFragments, listOf(getString(R.string.coupon_valid),getString(R.string.coupon_invalid)))
        binding.tlConponTab.setupWithViewPager(binding.mvpCoupon)
```
# toast
```
              toast("我是toast")
```
# 屏幕适配  
  在节点中添加即可
```
<manifest>
    <application>            
        <meta-data
            android:name="design_width_in_dp"
            android:value="360"/>
        <meta-data
            android:name="design_height_in_dp"
            android:value="640"/>           
     </application>           
</manifest>
```











