package com.example.exoskeletonhelper

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.exoskeletonhelper.databinding.ActivityMainBinding
import com.example.myapplication.utils.BaseActivity
import com.example.utils.myview.statusbar.StatusBarUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.MqttCallback

class MainActivity : BaseActivity<ActivityMainBinding>(),
    MainConstValue {
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    //用于存放Fragment的，为了初始化ViewPager
    private val fragmentList = ArrayList<Fragment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        //以StatusBarUtil开头的方法都是用于设置状态栏的
        //下面两个方法需要在setContentView之前
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        StatusBarUtil.setTranslucentStatus(this)
        super.onCreate(savedInstanceState)
        //初始化dataBinding
        initBinding()
        //如果状态栏字体的颜色不为黑色或者白色，则设置其他颜色
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }
        //true：支持右滑返回


        canSlideFinish(false)
        init()
    }

    //初始化一些东西，例如找view，并调用其他init方法
    private fun init() {
        viewPager = vp_main
        viewPager.offscreenPageLimit = 1
        tabLayout = tb_layout_main
        initViewPager()
        initTabLayout()
    }

    //初始化ViewPager
    private fun initViewPager() {
        //往fragmentList里添加各个Fragment，下方顺序不能乱
        //添加找团队Fragment
        fragmentList.add(HomeFragment.newInstance())
        //添加咨询Fragment
        fragmentList.add(SuggestFragment.newInstance())
        //添加社区Fragment

        fragmentList.add(ActionFragment.newInstance())

        fragmentList.add(EquipmentFragment.newInstance())
        //添加我的Fragment
        //为ViewPager设置Adapter
        viewPager.adapter = com.example.myapplication.utils.PagerAdapter(
            fragmentList,
            supportFragmentManager
        )
        //默认显示的是第一个找团队界面
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                //改变TabLayout图标
                tabLayout.getTabAt(position)?.select()
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })
    }

    //初始化TabLayout的内容
    private fun initTabLayout() {
        //为TabLayout添加内容
        for (i: Int in 0 until tabText.size) {
            if (i == 0) {
                tabLayout.addTab(
                    tabLayout.newTab().setText(tabText[0]).setIcon(
                        tabSelectedDrawableIdList[0]
                    )
                )
            } else {
                tabLayout.addTab(
                    tabLayout.newTab().setText(tabText[i]).setIcon(
                        tabUnselectedDrawableList[i]
                    )
                )
            }
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            //设置未选中图标
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.apply {
                    setIcon(tabUnselectedDrawableList[position])
                }
            }

            //设置选中图标
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply {
                    setIcon(tabSelectedDrawableIdList[position])
                    viewPager.currentItem = position
                }
            }
        })
    }


    //为返回给BaseActivity layoutId
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    companion object {
        //这个是为了回调Fragment的触摸事件
        private val onTouchListeners = ArrayList<MyOnTouchListener>()

        fun registerMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
            onTouchListeners.add(myOnTouchListener)
        }

        fun unregisterMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
            onTouchListeners.remove(myOnTouchListener)
        }
    }
}

interface MyOnTouchListener {
    fun onTouch(isScroll: Boolean)
}
