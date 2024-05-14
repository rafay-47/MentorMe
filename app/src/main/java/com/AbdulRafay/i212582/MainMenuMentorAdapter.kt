package com.AbdulRafay.i212582

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

import java.io.Serializable

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

data class Mentors(
    val id: String,
    val name: String,
    val desc: String,
    val price: String,
    val imageURL: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(price)
        parcel.writeString(imageURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mentors> {
        override fun createFromParcel(parcel: Parcel): Mentors {
            return Mentors(parcel)
        }

        override fun newArray(size: Int): Array<Mentors?> {
            return arrayOfNulls(size)
        }
    }
}


class MainMenuMentorAdapter(
    private val itemClassList: List<Mentors>,
    private val listener: OnAcceptButtonClickListener
) : RecyclerView.Adapter<MainMenuMentorAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        Log.d("MainMenu", "onCreate12: ")
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.main_menu_mentor_item, parent, false)
        Log.d("MainMenu", "onCreate13: ")
        return RecyclerViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        Log.d("MainMenu", "onCreate14: ")
        val title = itemClassList[position].name
        val desc = itemClassList[position].desc
        val price = itemClassList[position].price
        val image = itemClassList[position].imageURL
        holder.setViews(title, desc, price, image)
        holder.AcceptBtn.setOnClickListener {
            listener.onAcceptButtonClick(itemClassList[position])
        }
        Log.d("MainMenu", "onCreate15: ")
    }

    override fun getItemCount(): Int {
        return itemClassList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.name)
        private val desc: TextView = itemView.findViewById(R.id.desc)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val image: ImageView = itemView.findViewById(R.id.mentorImage)
        val AcceptBtn: ConstraintLayout = itemView.findViewById(R.id.main_menu_mentor_item)

        fun setViews(title: String?, desc: String?, price: String?, image: String?) {
            Log.d("MainMenu", "onCreate15: ")
            this.title.text = title
            this.desc.text = desc
            this.price.text = "$" + price + "/Session"
            Picasso.get().load(image).into(this.image);
            Log.d("MainMenu", "onCreate16: ")

        }
    }

    interface OnAcceptButtonClickListener {
        fun onAcceptButtonClick(mentor: Mentors)
    }
}
