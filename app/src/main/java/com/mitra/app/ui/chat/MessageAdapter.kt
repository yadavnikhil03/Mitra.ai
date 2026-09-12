package com.mitra.app.ui.chat

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mitra.app.data.model.ChatMessage
import com.mitra.app.data.model.MessageItem
import com.mitra.app.data.model.MessageType
import com.mitra.app.data.model.Role
import com.mitra.app.databinding.ItemMessageMitraBinding
import com.mitra.app.databinding.ItemMessageUserBinding
import com.mitra.app.databinding.ItemSupportCardBinding
import com.mitra.app.databinding.ItemTypingIndicatorBinding
import com.mitra.app.utils.toFormattedTime

private const val VT_MITRA   = 0
private const val VT_USER    = 1
private const val VT_TYPING  = 2
private const val VT_SUPPORT = 3

class MessageAdapter(
    private val onSpeak: ((String) -> Unit)? = null
) : ListAdapter<MessageItem, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<MessageItem>() {
            override fun areItemsTheSame(a: MessageItem, b: MessageItem): Boolean = when {
                a is MessageItem.Regular && b is MessageItem.Regular -> a.msg.id == b.msg.id
                a is MessageItem.Typing  && b is MessageItem.Typing  -> true
                a is MessageItem.Support && b is MessageItem.Support -> true
                else -> false
            }
            override fun areContentsTheSame(a: MessageItem, b: MessageItem): Boolean = a == b
        }
    }

    override fun getItemViewType(pos: Int) = when (val item = getItem(pos)) {
        is MessageItem.Regular -> if (item.msg.role == Role.USER) VT_USER else VT_MITRA
        is MessageItem.Typing  -> VT_TYPING
        is MessageItem.Support -> VT_SUPPORT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            VT_MITRA   -> MitraVH(ItemMessageMitraBinding.inflate(inf, parent, false))
            VT_USER    -> UserVH(ItemMessageUserBinding.inflate(inf, parent, false))
            VT_TYPING  -> TypingVH(ItemTypingIndicatorBinding.inflate(inf, parent, false))
            else       -> SupportVH(ItemSupportCardBinding.inflate(inf, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MessageItem.Regular -> when (holder) {
                is MitraVH  -> holder.bind(item.msg)
                is UserVH   -> holder.bind(item.msg)
                else        -> {}
            }
            is MessageItem.Typing  -> (holder as TypingVH).startAnim()
            is MessageItem.Support -> (holder as SupportVH).bind(item.card.text)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is TypingVH) holder.stopAnim()
        super.onViewRecycled(holder)
    }

    inner class MitraVH(private val b: ItemMessageMitraBinding) : RecyclerView.ViewHolder(b.root) {
        private var boundMessageId: String? = null

        fun bind(msg: ChatMessage) {
            b.tvMessage.text = msg.content
            b.tvTime.text = msg.timestamp.toFormattedTime()
            b.btnSpeak.setOnClickListener { onSpeak?.invoke(msg.content) }

            val isHelpline = msg.content.contains("14416") || msg.type == MessageType.SUPPORT
            if (isHelpline) {
                b.btnHelplineCall.visibility = View.VISIBLE
                b.btnHelplineCall.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+9114416"))
                    b.root.context.startActivity(intent)
                }
            } else {
                b.btnHelplineCall.visibility = View.GONE
            }

            b.msgContainer.setOnLongClickListener { v ->
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                copyToClipboard(b.root.context, msg.content)
                true
            }
            if (boundMessageId != msg.id) {
                boundMessageId = msg.id
                popIn(b.msgContainer)
            }
        }
    }

    inner class UserVH(private val b: ItemMessageUserBinding) : RecyclerView.ViewHolder(b.root) {
        private var boundMessageId: String? = null

        fun bind(msg: ChatMessage) {
            b.tvMessage.text = msg.content
            b.tvTime.text = msg.timestamp.toFormattedTime()
            b.msgContainer.setOnLongClickListener { v ->
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                copyToClipboard(b.root.context, msg.content)
                true
            }
            if (boundMessageId != msg.id) {
                boundMessageId = msg.id
                popIn(b.msgContainer)
            }
        }
    }

    inner class TypingVH(private val b: ItemTypingIndicatorBinding) : RecyclerView.ViewHolder(b.root) {
        private val dots = listOf(b.dot1, b.dot2, b.dot3)
        private val animators = mutableListOf<Animator>()

        fun startAnim() {
            popIn(b.typingBubble)
            stopAnim()
            dots.forEachIndexed { i, dot ->
                val up = ObjectAnimator.ofFloat(dot, "translationY", 0f, -6f).apply {
                    duration    = 420
                    startDelay  = (i * 160).toLong()
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode  = ObjectAnimator.REVERSE
                    interpolator = DecelerateInterpolator()
                }
                val fade = ObjectAnimator.ofFloat(dot, "alpha", 0.35f, 1f).apply {
                    duration    = 420
                    startDelay  = (i * 160).toLong()
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode  = ObjectAnimator.REVERSE
                }
                animators += up
                animators += fade
                up.start()
                fade.start()
            }
        }

        fun stopAnim() {
            animators.forEach { it.cancel() }
            animators.clear()
            dots.forEach { it.translationY = 0f; it.alpha = 0.35f }
        }
    }

    inner class SupportVH(private val b: ItemSupportCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(text: String) {
            b.tvSupportText.text = text
            b.tvHelplineNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+9114416"))
                b.root.context.startActivity(intent)
            }
            popIn(b.root)
        }
    }

    private fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Message", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun popIn(view: View) {
        view.alpha       = 0f
        view.translationY = 12f
        view.scaleX      = 0.96f
        view.scaleY      = 0.96f
        view.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(380)
            .setInterpolator(DecelerateInterpolator(1.4f))
            .start()
    }
}
