package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.blue
import android.util.Log
import androidx.core.content.edit
import androidx.multidex.MultiDexApplication
import com.example.exoskeletonhelper.R
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MyApplication : MultiDexApplication() {

    init {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableLoadMore(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)
            MaterialHeader(context).setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableFooterTranslationContent(true)
            layout.setFooterHeight(153f)
            layout.setFooterTriggerRate(0.6f)

            ClassicsFooter.REFRESH_FOOTER_NOTHING = "- The End -"
            ClassicsFooter(context).apply {
                setAccentColorId(R.color.colorPrimary)
                setDrawableSize(16f)
            }
        }
    }



    override fun onCreate() {
        super.onCreate()
        context = this

    }

    companion object {
        lateinit var context: Context

        fun saveToken(token:String){
            val sp = context.getSharedPreferences("State", Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString("token",token)
            editor.apply()
            Log.e("token", "saveToken$token")
        }

        fun getToken(): String? {
            val sp = context.getSharedPreferences("State", Context.MODE_PRIVATE)
            val token = sp.getString("token","")
            Log.e("token", "getToken$token")
            return token

        }

    }
}