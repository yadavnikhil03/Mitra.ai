package com.mitra.app.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mitra.app.R
import com.mitra.app.data.model.Chat
import com.mitra.app.databinding.BottomSheetChatListBinding
import com.mitra.app.databinding.ItemChatRowBinding
import com.mitra.app.utils.toRelativeDate
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat

class ChatListSheet(
    private val chats: List<Chat>,
    private val activeChatId: String?,
    private val onSelect: (Chat) -> Unit,
    private val onDelete: (Chat) -> Unit,
    private val onNewChat: () -> Unit,
    private val onIncognito: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChatListBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.Theme_Mitra_BottomSheetDialog

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        _binding = BottomSheetChatListBinding.inflate(inf, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        super.onViewCreated(view, state)

        val adapter = ChatRowAdapter(
            chats       = chats.toMutableList(),
            activeChatId = activeChatId,
            onSelect    = { chat -> dismiss(); onSelect(chat) },
            onDelete    = { chat -> onDelete(chat) }
        )

        binding.recyclerChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter  = adapter
        }

        // Swipe-to-delete
        val swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private val paint = Paint().apply { color = 0xFFE89BA8.toInt() }
            private val cornerRadius = 14f * resources.displayMetrics.density

            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val chat = adapter.getItemAt(vh.adapterPosition)
                adapter.removeAt(vh.adapterPosition)
                onDelete(chat)
            }

            override fun onChildDraw(c: Canvas, rv: RecyclerView,
                                     vh: RecyclerView.ViewHolder,
                                     dX: Float, dY: Float,
                                     actionState: Int, isActive: Boolean) {
                // Draw rose-tinted delete background on swipe
                val itemView = vh.itemView
                if (dX < 0) {
                    val bg = RectF(
                        itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat()
                    )
                    c.drawRoundRect(bg, cornerRadius, cornerRadius, paint)
                }
                super.onChildDraw(c, rv, vh, dX, dY, actionState, isActive)
            }
        })
        swipeHelper.attachToRecyclerView(binding.recyclerChats)

        val isIncognitoActive = chats.find { it.id == activeChatId }?.isIncognito ?: false
        if (isIncognitoActive) {
            binding.btnIncognito.text = getString(R.string.exit_incognito)
            binding.btnIncognito.setOnClickListener { dismiss(); onNewChat() }
        } else {
            binding.btnIncognito.text = getString(R.string.incognito)
            binding.btnIncognito.setOnClickListener { dismiss(); onIncognito() }
        }

        binding.btnNewChat.setOnClickListener { dismiss(); onNewChat() }
        binding.btnClose.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }

    // ── Inner adapter ─────────────────────────────────────────────────────────
    private inner class ChatRowAdapter(
        private val chats: MutableList<Chat>,
        private val activeChatId: String?,
        private val onSelect: (Chat) -> Unit,
        private val onDelete: (Chat) -> Unit
    ) : RecyclerView.Adapter<ChatRowAdapter.VH>() {

        fun getItemAt(pos: Int) = chats[pos]
        fun removeAt(pos: Int)  { chats.removeAt(pos); notifyItemRemoved(pos) }

        inner class VH(val b: ItemChatRowBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
            VH(ItemChatRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount() = chats.size

        override fun onBindViewHolder(vh: VH, pos: Int) {
            val chat = chats[pos]
            vh.b.tvChatTitle.text = chat.displayTitle()
            vh.b.tvChatDate.text  = chat.updatedAt.toRelativeDate()
            vh.b.root.isActivated = chat.id == activeChatId
            vh.b.chatRowMain.setOnClickListener { onSelect(chat) }
            vh.b.btnDeleteChat.setOnClickListener { onDelete(chat); removeAt(pos) }
        }
    }
}
