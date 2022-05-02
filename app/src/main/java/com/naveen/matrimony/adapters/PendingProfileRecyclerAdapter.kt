package com.naveen.matrimony.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.naveen.matrimony.R
import com.naveen.matrimony.databinding.ItemUserBinding
import com.naveen.matrimony.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class PendingProfileRecyclerAdapter(
    private val context: Context,
    var pendingList: ArrayList<User>
) : RecyclerView.Adapter<PendingProfileRecyclerAdapter.ViewHolder>() {

    var onItemClicked: ((Int) -> Unit)? = null
    var onYesClicked: ((Int) -> Unit)? = null
    var onNoClicked: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingProfileRecyclerAdapter.ViewHolder {
        val employeeListItemBinding: ItemUserBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_user, parent, false
        )

        return ViewHolder(employeeListItemBinding)
    }

    override fun onBindViewHolder(holder: PendingProfileRecyclerAdapter.ViewHolder, position: Int) {
        val user = pendingList[position]
        holder.itemBinding?.user = user
        holder.itemBinding?.executePendingBindings();
        holder.src = pendingList[position].profileImgList?.get(0) ?: ""

    }

    override fun getItemCount(): Int {
        return pendingList.size
    }


    inner class ViewHolder(itemView: ItemUserBinding) : RecyclerView.ViewHolder(itemView.root) {

        var itemBinding: ItemUserBinding? = null
        var src: String = ""


        init {
            this.itemBinding = itemView
            itemView.viewHolder = this


            itemView.root.setOnClickListener {
                onItemClicked?.invoke(adapterPosition)
            }

            itemView.root.apply {
                yesTV.setOnClickListener {
                    onYesClicked?.invoke(adapterPosition)
                }
                noTV.setOnClickListener {
                    onNoClicked?.invoke(adapterPosition)
                }


            }


        }

    }


}