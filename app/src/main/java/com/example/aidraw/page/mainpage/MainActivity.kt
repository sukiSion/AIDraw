package com.example.aidraw.page.mainpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.aidraw.R
import com.example.aidraw.databinding.ActivityMainBinding
import com.example.aidraw.page.mainpage.homepage.HomeFragment
import com.example.aidraw.page.mainpage.settingpage.SettingFragment
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SettingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener

class MainActivity : AppCompatActivity() {


    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var bottomNavLayout: BottomNavigationView
    private lateinit var containerLayout: ViewPager2
    private lateinit var fragments: List<Fragment>
    private lateinit var pageAdapter: PageAdapter
    private lateinit var settingFragment: SettingFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var pageChangeCallback: OnPageChangeCallback
    private lateinit var bottomItemSelectedListener: NavigationBarView.OnItemSelectedListener
    private val settingViewModel: SettingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(activityMainBinding.root)
        val layouParams: ConstraintLayout.LayoutParams = activityMainBinding.containerLayout.layoutParams as LayoutParams
        layouParams.topMargin = ExUtil.getStatusBarHeight(this)
        ExUtil.transparentStatusBar(this)
        init()
    }

    private fun initData(){
        bottomNavLayout = activityMainBinding.navigationBottomLayout
        containerLayout  = activityMainBinding.containerLayout
        homeFragment = HomeFragment()
        settingFragment = SettingFragment()
        fragments = listOf(
            homeFragment,
            settingFragment
        )
        pageAdapter = PageAdapter(
            fragments,
            this
        )
        pageChangeCallback = object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        bottomNavLayout.selectedItemId = R.id.home_item
                    }
                    1 -> {
                        bottomNavLayout.selectedItemId = R.id.setting_item
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        }

        bottomItemSelectedListener = object : OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.home_item ->  {
                        containerLayout.currentItem = 0
                    }
                    R.id.setting_item -> {
                        containerLayout.currentItem = 1
                    }
                }
                return true
            }
        }
    }

    private fun init(){
        initData()
        initWidget()
    }

    private fun  initWidget(){
        bottomNavLayout.itemIconTintList = null

        bottomNavLayout.setOnItemSelectedListener(bottomItemSelectedListener)
        containerLayout.adapter = pageAdapter
        containerLayout.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        // 注册滑动监听事件
        containerLayout.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}