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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.xueng.driver.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.xueng.driver.app


/**
 * 在项目layout目录下创建`layout_engine_toolbar.xml`可以覆写标题栏布局
 */
abstract class DriverToolbarActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    DriverBaseActivity<B>(contentLayoutId) {

    companion object{
        const val FRAME=1
        const val LINEAR=2
    }

    lateinit var actionbar: ConstraintLayout
    lateinit var actionLeft: TextView
    lateinit var actionRight: TextView
    lateinit var actionTitle: TextView
    /**
     * 构建一个Toolbar
     */
    protected fun onCreateToolbar(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_driver_toolbar, container, false)
    }

    override fun setTitle(title: CharSequence?) {
        if (this::actionTitle.isInitialized) actionTitle.text = title ?: return
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    override fun setTitleColor(color: Int){
        if (this::actionbar.isInitialized) actionbar.setBackgroundColor(getColor(color))
    }

    fun setLight(){
        if (this::actionLeft.isInitialized) actionLeft.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(app,R.drawable.ic_action_left_white),null,null,null)
        if (this::actionTitle.isInitialized) actionTitle.setTextColor(getColor(R.color.white))
    }

    open fun getLayoutImmerSion() = FRAME


    @SuppressLint("InflateParams")
    override fun setContentView(layoutResId: Int) {
        val contentView:View
        if (getLayoutImmerSion()== FRAME){
            contentView = layoutInflater.inflate(R.layout.activity_driver_frame_toolbar, null, false)
        }else{
            contentView = layoutInflater.inflate(R.layout.activity_driver_linear_toolbar, null, false)
        }
        setContentView(contentView)
        val container = contentView as ViewGroup
        val toolbar = onCreateToolbar(layoutInflater, container)
        //region 设置沉浸式
        toolbar.setPadding(0,ImmersionBar.getStatusBarHeight(this),0,0)
        //endregion
        DataBindingUtil.bind<ViewDataBinding>(toolbar)
        container.addView(toolbar)
        binding = DataBindingUtil.inflate(layoutInflater, layoutResId, container, true)
        rootView = binding.root
        (findViewById<View>(R.id.actionbar) as? ConstraintLayout)?.let { actionbar = it }
        (findViewById<View>(R.id.actionTitle) as? TextView)?.let { actionTitle = it }
        (findViewById<View>(R.id.actionLeft) as? TextView)?.let { actionLeft = it }
        (findViewById<View>(R.id.actionRight) as? TextView)?.let { actionRight = it }
        if (this::actionLeft.isInitialized) actionLeft.setOnClickListener { onBack(it) }
        init()
        ImmersionBar.with(this).keyboardEnable(true).init()

    }


    //设置状态栏图标是不是黑色主题
    override fun setDrakState(boolean: Boolean){
        ImmersionBar.with(this).statusBarDarkFont(boolean)
    }


    open fun onBack(v: View) {
        finishTransition()
    }


}
