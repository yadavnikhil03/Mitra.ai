package com.mitra.app.ui.chat

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.mitra.app.R
import com.mitra.app.data.model.Chat
import com.mitra.app.data.model.MessageItem
import com.mitra.app.data.model.Role
import com.mitra.app.databinding.ActivityChatBinding
import com.mitra.app.databinding.ItemChatRowBinding
import com.mitra.app.ui.login.LoginActivity
import com.mitra.app.ui.sheets.ConfirmDeleteSheet
import com.mitra.app.ui.sheets.FeedbackSheet
import com.mitra.app.ui.sheets.InfoSheet
import com.mitra.app.utils.toRelativeDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val vm: ChatViewModel by viewModels()
    private lateinit var adapter: MessageAdapter
    private lateinit var drawerAdapter: DrawerChatAdapter

    @Inject lateinit var auth: FirebaseAuth

    private var speechRecognizer: SpeechRecognizer? = null
    private var isRecording = false
    private var baseText = ""

    private var tts: TextToSpeech? = null
    private var isTtsEnabled = false
    private var lastSpokenMessageId: String? = null

    private val micPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) startVoice() }

    private var glowBreathAnimator: ValueAnimator? = null
    private var isThinking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTts()
        applyWindowInsets()
        setupRecycler()
        setupDrawerRecycler()
        setupComposer()
        setupHeaderButtons()
        startGlowBreath()
        binding.particleView.start()

        val user = auth.currentUser
        if (user == null) {
            goToLogin(); return
        }
        lifecycleScope.launch {
            val token = try { user.getIdToken(false).result?.token } catch (_: Exception) { null }
            vm.onSignedIn(user.uid, token)
        }

        observeState()
        observeEvents()
    }

    private fun initTts() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
    }

    private fun speak(text: String) {
        if (text.isBlank()) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "MitraTTS")
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.header) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, bars.top + 8, v.paddingRight, v.paddingBottom)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.composerContainer) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val imeBars = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(navBars.bottom, imeBars.bottom) + 10
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, bottomPadding)
            insets
        }
    }

    private fun setupRecycler() {
        adapter = MessageAdapter(onSpeak = { text -> speak(text) })
        val llm = LinearLayoutManager(this).also { it.stackFromEnd = true }
        binding.recyclerMessages.apply {
            layoutManager = llm
            adapter = this@ChatActivity.adapter
            itemAnimator = null

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(windowToken, 0)
                    }
                }
            })
        }
    }

    private fun setupDrawerRecycler() {
        drawerAdapter = DrawerChatAdapter(
            onSelect = { chat ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                vm.switchChat(chat.id)
            },
            onDelete = { chat ->
                vm.deleteChat(chat.id)
            }
        )
        binding.recyclerDrawerChats.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = drawerAdapter
        }
    }

    private fun setupComposer() {
        binding.etMessage.setOnFocusChangeListener { _, focused ->
            binding.etMessage.setBackgroundResource(
                if (focused) R.drawable.bg_composer_input_focused
                else R.drawable.bg_composer_input
            )
        }

        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrBlank()
                val isBusy = vm.state.value.isBusy
                val targetAlpha = if (hasText && !isBusy) 1f else 0.45f
                binding.btnSend.animate().alpha(targetAlpha).setDuration(150).start()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text?.toString()?.trim() ?: ""
            if (text.isNotEmpty()) {
                vm.sendMessage(text)
                binding.etMessage.setText("")
            }
        }

        binding.etMessage.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER &&
                event.action == KeyEvent.ACTION_DOWN &&
                !event.isShiftPressed) {
                binding.btnSend.performClick()
                true
            } else false
        }

        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            binding.btnMic.visibility = View.VISIBLE
            setupSpeechRecognizer()
            binding.btnMic.setOnClickListener { onMicClick() }
        }
    }

    private fun setupHeaderButtons() {
        binding.btnChatList.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnTts.setOnClickListener {
            isTtsEnabled = !isTtsEnabled
            if (isTtsEnabled) {
                binding.btnTts.setImageResource(R.drawable.ic_volume_up)
                Toast.makeText(this, getString(R.string.tts_on), Toast.LENGTH_SHORT).show()
            } else {
                tts?.stop()
                binding.btnTts.setImageResource(R.drawable.ic_volume_off)
                Toast.makeText(this, getString(R.string.tts_off), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHeaderNewChat.setOnClickListener { vm.newChat() }
        binding.btnInfo.setOnClickListener { showInfoSheet() }
        binding.incognitoBanner.setOnClickListener { vm.newChat() }

        binding.btnDrawerNewChat.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            vm.newChat()
        }
        binding.btnDrawerIncognito.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val isIncognitoActive = vm.state.value.isIncognito
            if (isIncognitoActive) vm.newChat() else vm.newIncognitoChat()
        }

        binding.chipSuggestion1.setOnClickListener {
            vm.sendMessage(getString(R.string.suggestion_1_prompt))
        }
        binding.chipSuggestion2.setOnClickListener {
            vm.sendMessage(getString(R.string.suggestion_2_prompt))
        }
        binding.chipSuggestion3.setOnClickListener {
            vm.sendMessage(getString(R.string.suggestion_3_prompt))
        }
        binding.chipSuggestion4.setOnClickListener {
            vm.sendMessage(getString(R.string.suggestion_4_prompt))
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { state ->
                    adapter.submitList(state.items.toList()) {
                        if (adapter.itemCount > 0) {
                            binding.recyclerMessages.scrollToPosition(adapter.itemCount - 1)
                        }
                    }

                    drawerAdapter.setActiveId(state.activeChatId)
                    drawerAdapter.submitList(state.allChats.toList())

                    val hasUserMessages = state.items.any { item ->
                        item is MessageItem.Regular && item.msg.role == Role.USER
                    }

                    binding.recyclerMessages.visibility = View.VISIBLE
                    binding.suggestionContainer.visibility = if (hasUserMessages) View.GONE else View.VISIBLE

                    binding.btnSend.alpha = if (state.isBusy) 0.4f else 1f
                    binding.btnSend.isEnabled = !state.isBusy

                    if (state.isIncognito) {
                        binding.mainContent.setBackgroundColor(getColor(R.color.incognito_bg))
                        binding.header.setBackgroundColor(getColor(R.color.incognito_bg))
                        binding.composerContainer.setBackgroundColor(getColor(R.color.incognito_bg))
                        binding.etMessage.hint = "type incognito message…"
                        binding.incognitoBanner.visibility = View.VISIBLE
                        binding.glowOrb.alpha = 0.25f

                        binding.btnDrawerIncognito.text = getString(R.string.exit_incognito)
                        binding.btnDrawerIncognito.setBackgroundResource(R.drawable.bg_incognito_active_chip)
                        binding.btnDrawerIncognito.setTextColor(0xFFFFFFFF.toInt())
                    } else {
                        binding.mainContent.setBackgroundColor(getColor(R.color.night_edge))
                        binding.header.setBackgroundColor(getColor(R.color.night_edge))
                        binding.composerContainer.setBackgroundColor(getColor(R.color.night_edge))
                        binding.etMessage.hint = getString(R.string.type_anything)
                        binding.incognitoBanner.visibility = View.GONE
                        binding.glowOrb.alpha = 0.7f

                        binding.btnDrawerIncognito.text = getString(R.string.incognito)
                        binding.btnDrawerIncognito.setBackgroundResource(R.drawable.bg_ghost_chip)
                        binding.btnDrawerIncognito.setTextColor(getColor(R.color.muted))
                    }

                    val lastItem = state.items.lastOrNull()
                    if (isTtsEnabled && !state.isBusy && lastItem is MessageItem.Regular && lastItem.msg.role == Role.MITRA) {
                        if (lastSpokenMessageId != lastItem.msg.id) {
                            lastSpokenMessageId = lastItem.msg.id
                            speak(lastItem.msg.content)
                        }
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.events.collect { event ->
                    when (event) {
                        is ChatEvent.ScrollToBottom -> {
                            val last = adapter.itemCount - 1
                            if (last >= 0) binding.recyclerMessages.smoothScrollToPosition(last)
                        }
                        is ChatEvent.NavigateToLogin -> goToLogin()
                        is ChatEvent.ShowToast -> Toast.makeText(this@ChatActivity, event.msg, Toast.LENGTH_LONG).show()
                        is ChatEvent.ShowGlowThinking -> setGlowThinking(event.on)
                    }
                }
            }
        }
    }

    private fun startGlowBreath() {
        glowBreathAnimator?.cancel()
        glowBreathAnimator = ValueAnimator.ofFloat(1f, 1.08f, 1f).apply {
            duration = 6500
            repeatCount = ValueAnimator.INFINITE
            interpolator = DecelerateInterpolator()
            addUpdateListener { anim ->
                val v = anim.animatedValue as Float
                binding.glowOrb.scaleX = v
                binding.glowOrb.scaleY = v
            }
            start()
        }
    }

    private fun setGlowThinking(thinking: Boolean) {
        if (isThinking == thinking) return
        isThinking = thinking
        glowBreathAnimator?.cancel()
        glowBreathAnimator = if (thinking) {
            ValueAnimator.ofFloat(1f, 1.1f, 1f).apply {
                duration = 2400
                repeatCount = ValueAnimator.INFINITE
                interpolator = DecelerateInterpolator()
                addUpdateListener { anim ->
                    val v = anim.animatedValue as Float
                    binding.glowOrb.scaleX = v
                    binding.glowOrb.scaleY = v
                }
                start()
            }
        } else {
            startGlowBreath(); null
        }
    }

    private fun showInfoSheet() {
        InfoSheet(
            onFeedback = { showFeedbackSheet() },
            onDeleteAll = {
                ConfirmDeleteSheet { vm.deleteAllChats() }.show(supportFragmentManager, "confirm_delete")
            },
            onLogout = { vm.logout() }
        ).show(supportFragmentManager, "info")
    }

    private fun showFeedbackSheet() {
        FeedbackSheet(
            onSend = { text -> vm.sendFeedback(text) }
        ).show(supportFragmentManager, "feedback")
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                setMicRecording(true)
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                if (text.isNotEmpty()) {
                    val sep = if (baseText.isNotEmpty()) " " else ""
                    val full = (baseText + sep + text).trim()
                    binding.etMessage.setText(full)
                    binding.etMessage.setSelection(full.length)
                    binding.btnSend.performClick()
                }
                setMicRecording(false)
            }
            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull() ?: return
                val sep = if (baseText.isNotEmpty()) " " else ""
                binding.etMessage.setText((baseText + sep + partial).trim())
            }
            override fun onError(error: Int) { setMicRecording(false) }
            override fun onEndOfSpeech() {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun onMicClick() {
        if (isRecording) {
            speechRecognizer?.stopListening()
            setMicRecording(false)
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        startVoice()
    }

    private fun startVoice() {
        baseText = binding.etMessage.text?.toString()?.trim() ?: ""
        binding.etMessage.hint = getString(R.string.listening)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        speechRecognizer?.startListening(intent)
    }

    private fun setMicRecording(recording: Boolean) {
        isRecording = recording
        if (recording) {
            binding.btnMic.setImageResource(R.drawable.ic_mic_stop)
            binding.btnMic.setBackgroundResource(R.drawable.bg_mic_recording)
            binding.btnMic.imageTintList =
                ColorStateList.valueOf(getColor(R.color.night_edge))
            ObjectAnimator.ofFloat(binding.btnMic, "scaleX", 1f, 1.08f, 1f).apply {
                duration = 1400
                repeatCount = ObjectAnimator.INFINITE
                start()
            }
            ObjectAnimator.ofFloat(binding.btnMic, "scaleY", 1f, 1.08f, 1f).apply {
                duration = 1400
                repeatCount = ObjectAnimator.INFINITE
                start()
            }
        } else {
            binding.btnMic.setImageResource(R.drawable.ic_mic)
            binding.btnMic.setBackgroundResource(R.drawable.bg_icon_btn)
            binding.btnMic.imageTintList = null
            binding.btnMic.clearAnimation()
            binding.btnMic.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            binding.etMessage.hint = getString(R.string.type_anything)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.particleView.start()
    }

    override fun onPause() {
        super.onPause()
        binding.particleView.stop()
    }

    override fun onDestroy() {
        binding.particleView.stop()
        glowBreathAnimator?.cancel()
        speechRecognizer?.destroy()
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private inner class DrawerChatAdapter(
        private val onSelect: (Chat) -> Unit,
        private val onDelete: (Chat) -> Unit
    ) : ListAdapter<Chat, DrawerChatAdapter.VH>(DIFF) {

        private var activeId: String? = null

        fun setActiveId(id: String?) {
            activeId = id
            notifyDataSetChanged()
        }

        inner class VH(val b: ItemChatRowBinding) : RecyclerView.ViewHolder(b.root)

        override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
            VH(ItemChatRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(vh: VH, pos: Int) {
            val chat = getItem(pos)
            vh.b.tvChatTitle.text = chat.displayTitle()
            vh.b.tvChatDate.text = chat.updatedAt.toRelativeDate()
            vh.b.root.isActivated = chat.id == activeId
            vh.b.chatRowMain.setOnClickListener { onSelect(chat) }
            vh.b.btnDeleteChat.setOnClickListener { onDelete(chat) }
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(a: Chat, b: Chat) = a.id == b.id
            override fun areContentsTheSame(a: Chat, b: Chat) = a == b
        }
    }
}
