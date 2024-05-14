package com.AbdulRafay.i212582

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


data class Message(
    var messageid: String? = null,
    var message: String? = null,
    var messageType: String? = null,
    var senderid: String? = null,
    var receiverid: String? = null,
)
class MessageAdapter(
    val context: Context,
    val messageList: ArrayList<Message>,
    val messageClickListener: MessageClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.image_send, parent, false)
                ImageViewHolder(view)
            }
            2 -> {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.message_send, parent, false)
                MessageViewHolder(view)
            }
            3 -> {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.video_sent, parent, false)
                VideoViewHolder(view)
            }
            4 -> {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.message_send, parent, false)
                FileViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (holder.itemViewType) {
            1 -> {
                val viewHolder = holder as ImageViewHolder
                Picasso.get().load(currentMessage.message).into(viewHolder.sentImage)
                viewHolder.sentImage.setOnClickListener {
                    messageClickListener.onMessageClick(viewHolder.sentImage, currentMessage)
                }
            }
            2 -> {
                val viewHolder = holder as MessageViewHolder
                viewHolder.sentMessage.text = currentMessage.message
                viewHolder.sentMessage.setOnClickListener {
                    messageClickListener.onMessageClick(viewHolder.sentMessage, currentMessage)
                }
            }
            3 -> {
                val viewHolder = holder as VideoViewHolder
                currentMessage.message?.let { videoUrl ->
                    val videoUri = Uri.parse(videoUrl)
                    viewHolder.sentVideoView.setVideoURI(videoUri)
                    viewHolder.sentVideoView.setOnClickListener {
                        messageClickListener.onMessageClick(viewHolder.sentVideoView, currentMessage)
                    }
                }
            }
            4 -> {
                val viewHolder = holder as FileViewHolder
                viewHolder.sentFile.text = currentMessage.message
                viewHolder.sentFile.setOnClickListener {
                    messageClickListener.onMessageClick(viewHolder.sentFile, currentMessage)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messageList[position].messageType) {
            "image" -> 1
            "text" -> 2
            "video" -> 3
            "file" -> 4
            else -> throw IllegalArgumentException("Invalid message type")
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentmessage)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentImage = itemView.findViewById<ImageView>(R.id.imageSend)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentVideoView = itemView.findViewById<VideoView>(R.id.videoSend)
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentFile = itemView.findViewById<TextView>(R.id.sentmessage)
    }

    interface MessageClickListener {
        fun onMessageClick(view: View, message: Message)
    }
}
