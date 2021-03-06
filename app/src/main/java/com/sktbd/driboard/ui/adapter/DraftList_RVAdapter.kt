package com.sktbd.driboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sktbd.driboard.R
import com.sktbd.driboard.data.model.DraftEntity
import com.sktbd.driboard.databinding.DraftItemBinding
import com.sktbd.driboard.databinding.ShotItemBinding
import com.sktbd.driboard.ui.viewmodel.DraftListViewModel
import kotlinx.android.synthetic.main.draft_item.view.*
import kotlinx.android.synthetic.main.draft_item.view.textViewTitle
import kotlinx.android.synthetic.main.shot_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class DraftList_RVAdapter(var draftList:List<DraftEntity>, var viewModel: DraftListViewModel) : RecyclerView.Adapter<DraftList_RVAdapter.VHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnItemClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.draft_item,parent,false)
        return VHolder(v,viewModel)
    }

    override fun getItemCount(): Int {
        return draftList.size
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val draft: DraftEntity =draftList[position]
        holder.bindView(draft)
        holder.itemView.DeleteBtn.bringToFront()
        if (onDeleteClickListener != null) {
            holder.itemView.DeleteBtn.setOnClickListener { onDeleteClickListener?.onclick(holder.itemView.DeleteBtn, position) }
        }
        if (onItemClickListener != null) {
            holder.itemView.textViewTitle.setOnClickListener { onItemClickListener?.onclick(holder.itemView.textViewTitle, position) }
        }
        //yyyyMMdd_HHmmSS
        val timeFormatForId = SimpleDateFormat("yyyyMMdd_HHmmSS")
        val timeStamp = timeFormatForId.parse(draft.draftID)
        val timeFormartForDisplay = SimpleDateFormat("MM/dd/yyyy HH:mm")
        var time = "Edited on " +timeFormartForDisplay.format(timeStamp)
        holder.itemView.textViewDescript.text = time
    }

    inner class VHolder(itemView:View,var viewModelBoard:DraftListViewModel):RecyclerView.ViewHolder(itemView) {

        var lbinding: DraftItemBinding?=null
        init{
            lbinding=DataBindingUtil.bind(itemView)
        }
        fun bindView(draft: DraftEntity){
            this.lbinding!!.draft = draft
            lbinding!!.executePendingBindings()
        }
    }
}

