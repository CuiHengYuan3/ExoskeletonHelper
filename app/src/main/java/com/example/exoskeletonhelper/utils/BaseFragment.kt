package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.exoskeletonhelper.R

abstract class BaseFragment<T : ViewDataBinding> : Fragment(), RequestLifecycle {


    protected lateinit var rootView: View

    protected  var binding: T?=null


    // 是否已经加载过数据
    private var mHasLoadedData = false

    //Fragment中由于服务器或网络异常导致加载失败显示的布局。
    private var loadErrorView: View? = null


    //Fragment中显示加载等待的控件。
    protected var loading: ProgressBar? = null

    //依附的Activity
    lateinit var activity: Activity

    //日志输出标志
    protected val TAG: String = this.javaClass.simpleName


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity()!!

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        rootView = view
        binding = DataBindingUtil.bind<T>(view)!!
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewWithoutData()
        observe()
    }


    //初始化一些View的属性
    abstract fun initViewWithoutData()



    abstract fun observe()

    override fun onResume() {
        super.onResume()
        if (!mHasLoadedData) {
            startLoading()
            loadDataOnce()
            mHasLoadedData = true
            loadFinished()
        }
    }


    //第一次进入页面时候加载数据
    abstract fun loadDataOnce()


    abstract fun getLayoutId(): Int


    @CallSuper
    override fun startLoading() {
        loading?.visibility = View.VISIBLE
        hideLoadErrorView()
    }

    /**
     * 加载完成，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFinished() {
        loading?.visibility = View.GONE
    }

    /**
     * 加载失败，将加载等待控件隐藏。
     */
    @CallSuper
    override fun loadFailed(msg: String?) {
        loading?.visibility = View.GONE
    }


    /**
     * 当Fragment中的加载内容服务器返回失败或网络异常，通过此方法显示提示界面给用户。
     *
     * @param tip 界面中的提示信息
     * @param block 点击屏幕重新加载，回调处理。
     */
    protected fun showLoadErrorView(tip: String, block: View.() -> Unit) {
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.loadErrorView)
            if (viewStub != null) {
                loadErrorView = viewStub.inflate()
                val loadErrorkRootView = loadErrorView?.findViewById<View>(R.id.loadErrorkRootView)
                loadErrorkRootView?.setOnClickListener {
                    it?.block()
                }
            }
        }
    }

    /**
     * 将load error view进行隐藏。
     */
    protected fun hideLoadErrorView() {
        loadErrorView?.visibility = View.GONE
    }

}