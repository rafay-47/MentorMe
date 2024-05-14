package com.AbdulRafay.i212582

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ChatsLayoutAdapter(
    private val itemClassList: List<Mentors>,
    private val listener: OnAcceptButtonClickListener
) : RecyclerView.Adapter<ChatsLayoutAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.chats_layout_item, parent, false)
        return RecyclerViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val title = itemClassList[position].name
        val image = itemClassList[position].imageURL
        holder.setViews(title, image)
        holder.AcceptBtn.setOnClickListener {
            listener.onAcceptButtonClick(itemClassList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemClassList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.name)
        private val image: ImageView = itemView.findViewById(R.id.image)
        val AcceptBtn: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)

        fun setViews(title: String?,image: String?) {
            this.title.text = title
            Picasso.get().load(image).into(this.image);

        }
    }

    interface OnAcceptButtonClickListener {
        fun onAcceptButtonClick(mentor: Mentors)
    }
}