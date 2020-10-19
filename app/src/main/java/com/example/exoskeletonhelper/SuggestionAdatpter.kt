package com.example.exoskeletonhelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.exoskeletonhelper.databinding.ItemSuggestion2Binding
import com.example.exoskeletonhelper.databinding.ItemSuggestionBinding
import com.example.myapplication.utils.MyApplication.Companion.context

class SuggestionAdatpter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val textList =
        listOf("阶段一", "阶段二", "阶段三", "阶段四", "阶段五", "阶段六", "阶段七", "阶段八", "阶段九")
    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSuggestionBinding>(
            layoutInflater,
            R.layout.item_suggestion,
            parent,
            false
        )
        if (viewType == 0) {

            return SuggestionViewHolder(binding)

        }

        if (viewType == 1) {
            val binding = DataBindingUtil.inflate<ItemSuggestion2Binding>(
                layoutInflater,
                R.layout.item_suggestion_2,
                parent,
                false
            )
            return SuggestionViewHolder2(binding)
        }
        return SuggestionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 9
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SuggestionViewHolder) holder.binding.txTop.text = textList[position]
        if (holder is SuggestionViewHolder2) holder.binding.txTop.text = textList[position]
    }


    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    inner class SuggestionViewHolder(
        binding: ItemSuggestionBinding
    ) :
        BaseViewHolder<ItemSuggestionBinding>(binding)

    inner class SuggestionViewHolder2(
        binding: ItemSuggestion2Binding
    ) :
        BaseViewHolder<ItemSuggestion2Binding>(binding)


}


abstract class BaseViewHolder<T : ViewDataBinding>(
    val binding: T,
    private val listener: OnItemCilckListener? = null
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        listener?.let {
            binding.root.setOnClickListener {
                val position = adapterPosition
                listener.onItemCilck(it, position)
            }
        }


    }

}
interface OnItemCilckListener {

    fun onItemCilck(view: View, position: Int)

}
