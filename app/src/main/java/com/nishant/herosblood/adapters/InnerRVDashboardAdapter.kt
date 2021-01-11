package com.nishant.herosblood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nishant.herosblood.R
import com.nishant.herosblood.data.UserData
import com.nishant.herosblood.ui.DonorProfileActivity
import kotlinx.android.synthetic.main.single_item_inner_rv_dashboard.view.*
import java.io.Serializable

class InnerRVDashboardAdapter(
    private var context: Context,
    private var users: ArrayList<UserData>
) : RecyclerView.Adapter<InnerRVDashboardAdapter.SingleBloodTypeUser>() {
    class SingleBloodTypeUser(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.userName
        var cardView: CardView = itemView.rvCardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleBloodTypeUser {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.single_item_inner_rv_dashboard,
            parent,
            false
        )
        return SingleBloodTypeUser(itemView)
    }

    override fun onBindViewHolder(holder: SingleBloodTypeUser, position: Int) {
        val current = users[position]
        holder.userName.text = current.name

        holder.cardView.setOnClickListener {
            val intent = Intent(context, DonorProfileActivity::class.java)
            intent.putExtra("UserData", current as Serializable)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = users.size
}