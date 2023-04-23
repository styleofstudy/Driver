/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xueng.driver.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.system.exitProcess


/** activity*/
open class AbsBaseActivity(contentLayoutId: Int = 0) : AppCompatActivity(contentLayoutId) {

    companion object{
        const val TAG="AbsBaseActivity"
        private lateinit var mActivities:Stack<AbsBaseActivity>
        fun mActivitiesIsInit():Boolean= ::mActivities.isInitialized
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!mActivitiesIsInit()) {
            mActivities= Stack()
        }
        mActivities.push(this)
    }


    /**
     * 关闭所有指定tag的activity
     * activity 的 tag 可重载getTAG进行设置;
     *
     * @param tag 需要关闭的tag值
     */
    open fun finishActivities(tag: String?) {
        if (mActivities.empty()) {
            return
        }
        val temp: Stack<AbsBaseActivity> = Stack<AbsBaseActivity>()
        for (activity in mActivities) {
            if (activity != null && activity.getTag() == tag) {
                temp.push(activity)
            }
        }
        for (activity in temp) {
            mActivities.remove(activity)
            activity.finish()
        }
    }

    open fun getTag(): String = TAG


    /**
     * 返回指定的Activity
     * @param cls 返回的具体activity
     */
    @Deprecated("")
    open fun backToActivity(cls: Class<out AbsBaseActivity?>?) {
        val intent = Intent(this, cls)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    /**
     * 关闭客户端
     *
     * @param isAppExit 标示要不要退出app
     * true 标示退出app
     * false 标示清空activity堆栈
     */
    open fun exit(isAppExit: Boolean) {
        if (mActivities.empty()) {
            return
        }
        for (activity in mActivities) {
            if (activity != null && !activity.isFinishing) activity.finish()
        }
        mActivities.clear()
        if (isAppExit) {
            exitProcess(0)
        }
    }

}