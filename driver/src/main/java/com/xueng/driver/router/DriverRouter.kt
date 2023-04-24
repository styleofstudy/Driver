package com.xueng.driver.router

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.util.Log
import com.xueng.driver.app.DriverActivityResult
import com.xueng.driver.app.DriverBundle
import java.lang.ref.WeakReference

/**
 * @Function activity跳转路由
 */
object DriverRouter {

    private val tag = this.javaClass.name
    private var isdebug = true

    //当前发起跳转的上下文
    private var mContext = WeakReference<Context>(null)

    //目标界面的路径
    private var mClassPath = ""

    //跳转界面需要传递的参数
    private var mParams = HashMap<String, Any?>()

    //intent需要设置的模式
    private var mCategory = ""
    private var mFlag = -1

    //开启转场动画时需要的上下文
    private var currentActivity = WeakReference<Activity>(null)

    //转场动画的id
    private var mEnterAnimID = 0
    private var mExitAnimID = 0

    private var mAction = Intent.ACTION_MAIN
    private var mData: Uri? = null
    private var mType = ""

    /**
     * 获取上下文环境对象
     * @Params context
     * @Details 此方法只需要在Application的oncreate里初始化一次即可
     */
    fun init(context: Context?) {
        if (context == null) {
            if (isdebug) {
                Log.d(tag, "this context is null")
            }
            return
        }
        mContext = WeakReference(context)
    }


    /**
     * 获取目标界面的路径
     * @Params classPath
     */
    fun build(cls: Class<*>?): DriverRouter {
        if (cls == null) return this
        return build(cls.name)
    }

    /**
     * 获取目标界面的路径
     * @Params classPath
     */
    fun build(classPath: String?): DriverRouter {
        if (classPath.isNullOrEmpty()) {
            if (isdebug) {
                Log.d(tag, "this classPath is null")
            }
        } else {
            /**
             * 先将上次设置的数据清除，防止被本次复用
             *
             * （这里主要考虑的场景是用户设置完数据后最后并没有调用start()方法而产生的数据缓存）
             */
            clearCache()

            mClassPath = classPath ?: ""
        }
        return this
    }


    /**
     * 获取传递的参数
     * @Params  key/value
     */
    fun put(key: String, value: Any?): DriverRouter {
        mParams[key] = value
        return this
    }

    /**
     * 获取传递的参数
     * @Params  k/v = map
     */
    fun put(paramsMap: HashMap<String, Any?>): DriverRouter {
        mParams = paramsMap
        return this
    }

    /**
     * 获取设置intent的参数
     * @Params category
     */
    fun addCategory(category: String): DriverRouter {
        mCategory = category
        return this
    }

    /**
     * 获取设置intent的参数
     * @Params flags = int
     */
    fun addFlags(flags: Int): DriverRouter {
        mFlag = flags
        return this
    }

    /**
     * 添加Intent的action
     * @Params action = String
     */
    fun addAction(action: String): DriverRouter {
        mAction = action
        return this
    }

    /**
     * 添加data
     * @Params data = Uri
     */
    fun addData(uri: Uri): DriverRouter {
        mData = uri
        return this
    }

    /**
     * 添加type
     * @Params type = String
     */
    fun addType(type: String): DriverRouter {
        mType = type
        return this
    }

    /**
     * 获取转场动画的参数
     * @Params fromContext 当前界面的上下文
     * @Params enterAnim 旧界面退出时的动画
     * @Params exitAnim 新界面时的动画
     */
    fun transitionAnim(fromContext: Activity?, enterAnimID: Int, exitAnimID: Int): DriverRouter {
        if (fromContext == null || fromContext.isFinishing) {
            if (isdebug) {
                Log.d(tag, "this context is null or isFinishing")
            }
            return this
        }
        mEnterAnimID = enterAnimID
        mExitAnimID = exitAnimID

        currentActivity = WeakReference(fromContext)
        return this
    }

    /**
     * 启动跳转
     */
    fun start() {
        launchUI(null, null,null)
    }

    /** open(fromContext, requestCode)
     * 启动有回调的跳转
     */
    fun start(fromContext: Activity?, requestCode: Int?,callback:((resultCode:Int, data:Intent?) -> Unit)?) {
        launchUI(fromContext, requestCode,callback)
    }


    private fun launchUI(fromContext: Activity?, requestCode: Int?, callback:((resultCode:Int, data:Intent?) -> Unit)?) {
        if (mContext.get() == null) {
            if (isdebug) {
                Log.d(tag, "this context is null")
            }
            return
        }

        open(fromContext, requestCode,callback)
    }

    private fun open(
        fromContext: Activity?,
        requestCode: Int?,
        callback:((resultCode:Int, data:Intent?) -> Unit)?
    ) {
        val intent = Intent(mAction)

        //判断是否有需要传递的参数
        if (mParams.isNotEmpty()) {

            val bundle = DriverBundle.create()
            mParams.forEach {
                val key = it.key
                val value = it.value
                if (value != null)
                    bundle.put(key, value.toString())
            }
            intent.putExtras(bundle.bundle)
        }

        if (mData != null)
            intent.data = mData
        if (mType.isNotEmpty())
            intent.type = mType

        //判断是否需要设置intent的category
        if (mCategory.isNotEmpty()) {
            intent.addCategory(mCategory)
        }

        //判断是否需要设置intent的flags
        if (mFlag != -1) {
            intent.addFlags(mFlag)
        }
        //绑定要跳转的目标界面的路径
        if (mClassPath.isNotEmpty())
            intent.component = ComponentName(mContext.get()?.packageName!!, mClassPath)

        try {
            //启动跳转
            if (requestCode == null) {
                if (mFlag != FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                mContext.get()?.startActivity(intent)
            } else {
                mContext.get()?.let { DriverActivityResult.startActivity(it,intent,callback) }
               // fromContext?.startActivityForResult(intent, requestCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //判断是否需要开启转场动画
        if (currentActivity.get() != null)
            currentActivity.get()?.overridePendingTransition(mEnterAnimID, mExitAnimID)

        //用完后及时清除，防止被下次的复用
        clearCache()
    }

    private fun clearCache() {
        mParams = HashMap()
        mCategory = ""
        mFlag = -1
        mClassPath = ""

        //完成动画后将设置的id和上下文对象清空
        mEnterAnimID = 0
        mExitAnimID = 0
        currentActivity = WeakReference<Activity>(null)

        mAction = Intent.ACTION_MAIN
        mData = null
        mType = ""
    }

}