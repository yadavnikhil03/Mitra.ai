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
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.mitra.app.data.model.ChatMessage
import com.mitra.app.data.model.MessageItem
import com.mitra.app.data.model.Role
import com.mitra.app.databinding.ActivityChatBinding
import com.mitra.app.databinding.ItemChatRowBinding
import com.mitra.app.ui.login.LoginActivity
import com.mitra.app.ui.sheets.ConfirmDeleteSheet
import com.mitra.app.ui.sheets.FeedbackSheet
import com.mitra.app.ui.sheets.InfoSheet
import com.mitra.app.utils.toRelativeDate
import com.mitra.app.worker.ProactiveCheckInScheduler
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
    private var ttsReady = false
    private val spokenPerChat = mutableMapOf<String, String>()
    private var pendingSpeech: Pair<String, String>? = null
    private var activeObservedChatId: String = ""

    private lateinit var speechPlayer: SpeechPlayer
    private var faceLoaded = false

    private val speechCache: java.io.File by lazy {
        java.io.File(cacheDir, "mitra_tts").also { it.mkdirs() }
    }

    private val micPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) startVoice() }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    private var glowBreathAnimator: ValueAnimator? = null
    private var isThinking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBackPress()

        initTts()
        applyWindowInsets()
        setupRecycler()
        setupDrawerRecycler()
        setupComposer()
        setupHeaderButtons()
        setupFace()

        requestNotificationPermissionAndSchedule()

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

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun initTts() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val t = tts ?: return@TextToSpeech
                t.language = Locale.ENGLISH
                t.setPitch(1.03f)
                t.setSpeechRate(0.94f)

                val hdVoice = t.voices?.find { v ->
                    val name = v.name.lowercase()
                    v.locale.language == "en" &&
                    (name.contains("network") || name.contains("sfg") || name.contains("ena") || name.contains("iog") || name.contains("tpf"))
                }
                if (hdVoice != null) {
                    t.voice = hdVoice
                }
                speechPlayer = SpeechPlayer(t, speechCache) { value ->
                    if (faceLoaded) binding.l2dView.setMouth(value)
                }
                ttsReady = true
                val pending = pendingSpeech
                if (pending != null && isTtsEnabled) {
                    pendingSpeech = null
                    val msg = vm.state.value.items
                        .filterIsInstance<MessageItem.Regular>()
                        .map { it.msg }
                        .firstOrNull { it.id == pending.second && it.role == Role.MITRA }
                    if (msg != null) {
                        speak(msg.content)
                        spokenPerChat[pending.first] = msg.id
                    }
                }
            }
        }
    }

    private fun speak(text: String) {
        if (text.isBlank()) return
        if (!ttsReady) return

        val cleanText = text
            .replace(Regex("[*_~`#]"), "")
            .replace("...", "…")
            .replace("--", "—")

        val clauses = cleanText.split(Regex("(?<=[.,?!;…—\n])\\s+"))
        val units = mutableListOf<SpeechPlayer.SpeechClause>()
        for (clause in clauses) {
            val trimmed = clause.trim()
            if (trimmed.isEmpty()) continue

            val sentencePitch = when {
                trimmed.endsWith("?") -> 1.08f
                trimmed.endsWith("!") -> 1.06f
                else -> 1.03f
            }

            val pauseMs = when {
                trimmed.endsWith("…") || trimmed.endsWith("—") -> 620L
                trimmed.endsWith("?") -> 520L
                trimmed.endsWith(".") || trimmed.endsWith("!") -> 420L
                trimmed.endsWith(",") || trimmed.endsWith(";") -> 280L
                trimmed.contains("\n") -> 500L
                else -> 180L
            }
            units.add(SpeechPlayer.SpeechClause(trimmed, sentencePitch, pauseMs))
        }
        speechPlayer.speak(units)
    }

    private fun maybeAutoSpeak(chatId: String, msg: ChatMessage) {
        if (!isTtsEnabled) return
        if (msg.content.isBlank()) return
        if (spokenPerChat[chatId] == msg.id) return
        if (!ttsReady) {
            pendingSpeech = chatId to msg.id
            return
        }
        speak(msg.content)
        spokenPerChat[chatId] = msg.id
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.leftDrawerContainer) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, bars.top + 20, v.paddingRight, v.paddingBottom)
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
            if (text.isNotEmpty() && vm.sendMessage(text)) {
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

    private fun setupFace() {
        binding.l2dView.listener = object : L2DView.Listener {
            override fun onFaceReady() {
                runOnUiThread { faceLoaded = true }
            }
            override fun onFaceError(message: String) {
                runOnUiThread {
                    if (binding.avatarStage.visibility == View.VISIBLE) {
                        binding.avatarStage.visibility = View.GONE
                    }
                }
            }
        }
        binding.avatarStage.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val w = binding.avatarStage.width.toFloat()
                    val h = binding.avatarStage.height.toFloat()
                    if (w > 0 && h > 0) {
                        val nx = (event.x / w) * 2 - 1
                        val ny = 1 - (event.y / h) * 2
                        binding.l2dView.lookAt(nx, ny)
                    }
                }
                else -> {}
            }
            true
        }
        binding.l2dView.load()
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
                speechPlayer.stop()
                tts?.stop()
                binding.btnTts.setImageResource(R.drawable.ic_volume_off)
                Toast.makeText(this, getString(R.string.tts_off), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAvatar.setOnClickListener {
            val newVisibility =
                if (binding.avatarStage.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            binding.avatarStage.visibility = newVisibility
        }

        binding.btnHeaderNewChat.setOnClickListener { vm.newChat() }
        binding.btnInfo.setOnClickListener { showInfoSheet() }
        binding.incognitoBanner.setOnClickListener { vm.exitIncognito() }

        binding.btnDrawerNewChat.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            vm.newChat()
        }
        binding.btnDrawerIncognito.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val isIncognitoActive = vm.state.value.isIncognito
            if (isIncognitoActive) vm.exitIncognito() else vm.newIncognitoChat()
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
                        val rv = binding.recyclerMessages
                        if (adapter.itemCount <= 3 || !rv.canScrollVertically(1)) {
                            rv.scrollToPosition(adapter.itemCount - 1)
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

                    if (state.activeChatId != activeObservedChatId) {
                        activeObservedChatId = state.activeChatId
                        val lastMitra = state.items
                            .filterIsInstance<MessageItem.Regular>()
                            .lastOrNull { it.msg.role == Role.MITRA }
                        if (lastMitra != null) {
                            spokenPerChat[state.activeChatId] = lastMitra.msg.id
                        }
                    }

                    if (state.isIncognito) {
                        binding.mainContent.setBackgroundColor(getColor(R.color.incognito_bg))
                        binding.header.setBackgroundColor(getColor(R.color.incognito_bg))
                        binding.composerContainer.setBackgroundColor(getColor(R.color.incognito_bg))
                        binding.etMessage.hint = "type incognito message…"
                        binding.incognitoBanner.visibility = View.VISIBLE

                        binding.btnDrawerIncognito.text = getString(R.string.exit_incognito)
                        binding.btnDrawerIncognito.setBackgroundResource(R.drawable.bg_incognito_active_chip)
                        binding.btnDrawerIncognito.setTextColor(0xFFFFFFFF.toInt())
                    } else {
                        binding.mainContent.setBackgroundColor(getColor(R.color.night_edge))
                        binding.header.setBackgroundColor(getColor(R.color.night_edge))
                        binding.composerContainer.setBackgroundColor(getColor(R.color.night_edge))
                        binding.etMessage.hint = getString(R.string.type_anything)
                        binding.incognitoBanner.visibility = View.GONE

                        binding.btnDrawerIncognito.text = getString(R.string.incognito)
                        binding.btnDrawerIncognito.setBackgroundResource(R.drawable.bg_ghost_chip)
                        binding.btnDrawerIncognito.setTextColor(getColor(R.color.muted))
                    }

                    val lastItem = state.items.lastOrNull()
                    if (!state.isBusy && lastItem is MessageItem.Regular && lastItem.msg.role == Role.MITRA) {
                        maybeAutoSpeak(state.activeChatId, lastItem.msg)
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
                            if (last >= 0) {
                                val canScrollDown = binding.recyclerMessages.canScrollVertically(1)
                                if (!canScrollDown || adapter.itemCount <= 3) {
                                    binding.recyclerMessages.scrollToPosition(last)
                                }
                            }
                        }
                        is ChatEvent.NavigateToLogin -> goToLogin()
                        is ChatEvent.ShowToast -> Toast.makeText(this@ChatActivity, event.msg, Toast.LENGTH_LONG).show()
                        is ChatEvent.ShowGlowThinking -> setGlowThinking(event.on)
                    }
                }
            }
        }
    }

    private fun setGlowThinking(thinking: Boolean) {
        if (isThinking == thinking) return
        isThinking = thinking
        if (thinking) {
            glowBreathAnimator = ObjectAnimator.ofFloat(binding.brandDot, "alpha", 0.35f, 1f).apply {
                duration = 700
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
                start()
            }
        } else {
            glowBreathAnimator?.cancel()
            glowBreathAnimator = null
            binding.brandDot.alpha = 1f
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
                    if (vm.sendMessage(full)) {
                        binding.etMessage.setText("")
                    }
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
            override fun onError(error: Int) {
                setMicRecording(false)
                val message = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> getString(R.string.voice_no_match)
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> getString(R.string.voice_timeout)
                    SpeechRecognizer.ERROR_NETWORK -> getString(R.string.voice_network)
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> getString(R.string.voice_busy)
                    else -> getString(R.string.voice_error)
                }
                Toast.makeText(this@ChatActivity, message, Toast.LENGTH_SHORT).show()
            }
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
            binding.btnMic.scaleX = 1f
            binding.btnMic.scaleY = 1f
            binding.etMessage.hint = getString(R.string.type_anything)
        }
    }

    override fun onDestroy() {
        glowBreathAnimator?.cancel()
        speechRecognizer?.destroy()
        if (::speechPlayer.isInitialized) speechPlayer.stop()
        tts?.stop()
        tts?.shutdown()
        binding.l2dView.destroy()
        super.onDestroy()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun requestNotificationPermissionAndSchedule() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        ProactiveCheckInScheduler.schedule(this)
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