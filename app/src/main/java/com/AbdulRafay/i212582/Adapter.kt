package com.AbdulRafay.i212582

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class SearchResultsMentorAdapter(
    private val itemClassList: List<Mentors>,
    private val listener: OnAcceptButtonClickListener
) : RecyclerView.Adapter<SearchResultsMentorAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_items, parent, false)
        return RecyclerViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val title = itemClassList[position].name
        val desc = itemClassList[position].desc
        val price = itemClassList[position].price
        val image = itemClassList[position].imageURL
        holder.setViews(title, desc, price, image)
        holder.AcceptBtn.setOnClickListener {
            listener.onAcceptButtonClick(itemClassList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemClassList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.name)
        private val desc: TextView = itemView.findViewById(R.id.desc)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val image: ImageView = itemView.findViewById(R.id.profilePic)
        val AcceptBtn: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)

        fun setViews(title: String?, desc: String?, price: String?, image: String?) {
            this.title.text = title
            this.desc.text = desc
            this.price.text = "$" + price + "/Session"
            Picasso.get().load(image).into(this.image);

        }
    }

    interface OnAcceptButtonClickListener {
        fun onAcceptButtonClick(mentor: Mentors)
    }
}
