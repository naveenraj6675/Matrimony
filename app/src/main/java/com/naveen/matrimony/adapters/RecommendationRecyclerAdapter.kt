package com.naveen.matrimony.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.naveen.matrimony.R
import com.naveen.matrimony.databinding.ItemRecommendationBinding
import com.naveen.matrimony.model.RecommendedUser


class RecommendationRecyclerAdapter(
    var context: Context,
    var recommendedList: ArrayList<RecommendedUser>
) : RecyclerView.Adapter<RecommendationRecyclerAdapter.ViewHolder>() {

    var onAcceptClicked: ((RecommendedUser) -> Unit)? = null
    var onRejectClicked: ((RecommendedUser) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationRecyclerAdapter.ViewHolder {
        val employeeListItemBinding: ItemRecommendationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_recommendation, parent, false
        )

        return ViewHolder(employeeListItemBinding)
    }

    override fun onBindViewHolder(holder: RecommendationRecyclerAdapter.ViewHolder, position: Int) {
        val user = recommendedList[position]
        holder.itemBinding?.recommended = user
        holder.itemBinding?.countTV?.text =
            recommendedList[position].profileImgList?.size.toString()

    }

    override fun getItemCount(): Int {
        return recommendedList.size
    }

    inner class ViewHolder(itemView: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var itemBinding: ItemRecommendationBinding? = null

        init {
            this.itemBinding = itemView

            itemBinding?.acceptFL?.setOnClickListener {
                onAcceptClicked?.invoke(recommendedList[adapterPosition])
            }

            itemBinding?.rejectFL?.setOnClickListener {
                onRejectClicked?.invoke(recommendedList[adapterPosition])

            }


        }

    }

}