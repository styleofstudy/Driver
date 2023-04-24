package com.xueng.driver.fragment

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * @author LiuXueFeng
 * @description:
 * @date :2022/7/5 18:02
 */
class NavigationMediator(
    private val fragmentActivity: FragmentActivity,
    private val bnv: BottomNavigationView,
    private val vp2: ViewPager2,
    private val mutableList: MutableList<DriverBaseFragment<*>>,
    private val config: ((bnv: BottomNavigationView, vp2: ViewPager2) -> Unit)? = null
) {

    //存储bottomNavigationView的menu的item和其自身position的对应关系
    private val map = mutableMapOf<MenuItem, Int>()

    init {
        //初始化bnv的item和index对应关系
        bnv.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }

    /**
     * 关联BottomNavigationView和ViewPager2的选择关系
     */
    fun attach() {
        config?.invoke(bnv, vp2)
        vp2.adapter = NaviViewPagerAdapter(fragmentActivity, mutableList)
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bnv.selectedItemId = bnv.menu[position].itemId
            }
        })
        bnv.setOnNavigationItemSelectedListener { item ->
            vp2.currentItem = map[item] ?: error("没有对应${item.title}的ViewPager2的元素")
            true
        }
    }

    inner class NaviViewPagerAdapter(
        private val fragmentActivity: FragmentActivity,
        private val fragments: MutableList<DriverBaseFragment<*>>
    ) : FragmentStateAdapter(fragmentActivity) {


        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

    }

   open fun getNavMap(): MutableMap<MenuItem, Int> = map
}