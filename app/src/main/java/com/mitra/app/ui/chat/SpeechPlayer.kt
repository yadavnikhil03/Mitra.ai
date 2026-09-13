package com.mitra.app.ui.chat

import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.io.File
import kotlin.math.min
import kotlin.math.sqrt

internal class SpeechPlayer(
    private val tts: TextToSpeech,
    private val cacheDir: File,
    private val onMouth: (Float) -> Unit
) {

    data class SpeechClause(val text: String, val pitch: Float, val pauseMs: Long)
    private data class Job(val file: File, val unit: SpeechClause, var ready: Boolean = false)

    var onSpeechChange: ((Boolean) -> Unit)? = null

    var isPlaying = false
        private set

    private val main = Handler(Looper.getMainLooper())
    private val jobs = LinkedHashMap<Int, Job>()
    private var stopped = true
    private var pendingCount = 0
    private var queue = emptyList<Job>()
    private var player: MediaPlayer? = null
    private var visualizer: Visualizer? = null
    private var flapRunnable: Runnable? = null
    private var watchdog: Runnable? = null
    private var mouthNow = 0f

    fun speak(units: List<SpeechClause>) {
        stopInternal()
        if (units.isEmpty()) return
        stopped = false
        jobs.clear()
        pendingCount = units.size
        for ((index, unit) in units.withIndex()) {
            val file = File(cacheDir, "mitra_${index}_${System.nanoTime()}.wav")
            jobs[index] = Job(file, unit)
            try {
                tts.setPitch(unit.pitch)
                tts.synthesizeToFile(unit.text, null, file, "mitra_$index")
            } catch (e: Exception) {
                main.post {
                    jobs.remove(index)?.file?.delete()
                    tickSlot()
                }
            }
        }
        watchdog = Runnable {
            if (!stopped && pendingCount > 0) {
                pendingCount = 0
                beginPlayback()
            }
        }
        main.postDelayed(watchdog!!, 10000L)
    }

    fun stop() {
        stopInternal()
    }

    private fun stopInternal() {
        stopped = true
        releaseMedia()
        try { tts.stop() } catch (ignored: Exception) {}
        jobs.values.forEach { it.file.delete() }
        jobs.clear()
        pendingCount = 0
        watchdog?.let { main.removeCallbacks(it) }
        watchdog = null
        queue = emptyList()
        setPlaying(false)
        onMouthNow(0f)
    }

    private fun tickSlot() {
        if (stopped) return
        if (pendingCount > 0) pendingCount--
        if (pendingCount == 0) {
            watchdog?.let { main.removeCallbacks(it) }
            watchdog = null
            beginPlayback()
        }
    }

    private fun beginPlayback() {
        queue = jobs.entries
            .sortedBy { it.key }
            .map { it.value }
            .filter { it.ready }
        jobs.values
            .filter { !it.ready }
            .forEach { it.file.delete() }
        jobs.clear()
        if (queue.isEmpty()) {
            onMouthNow(0f)
            setPlaying(false)
            return
        }
        setPlaying(true)
        playAt(0)
    }

    private fun playAt(position: Int) {
        if (stopped) {
            onMouthNow(0f)
            return
        }
        if (position >= queue.size) {
            onMouthNow(0f)
            setPlaying(false)
            return
        }
        releaseMedia()
        val job = queue[position]
        val mp = MediaPlayer()
        player = mp
        try {
            mp.setDataSource(job.file.absolutePath)
            mp.setOnErrorListener { _, _, _ ->
                job.file.delete()
                releaseMedia()
                playAt(position + 1)
                true
            }
            mp.setOnCompletionListener {
                job.file.delete()
                releaseMedia()
                onMouthNow(0f)
                main.postDelayed({ playAt(position + 1) }, job.unit.pauseMs)
            }
            mp.setOnPreparedListener { prepared ->
                if (stopped) {
                    releaseMedia()
                    return@setOnPreparedListener
                }
                attachVisualizer(prepared.audioSessionId)
                prepared.start()
            }
            mp.prepareAsync()
        } catch (e: Exception) {
            job.file.delete()
            releaseMedia()
            playAt(position + 1)
        }
    }

    private fun attachVisualizer(sessionId: Int) {
        releaseVisualizer()
        try {
            val vis = Visualizer(sessionId)
            vis.setDataCaptureListener(
                object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(
                        analyzer: Visualizer?,
                        waveform: ByteArray?,
                        samplingRate: Int
                    ) {
                        if (waveform == null || waveform.isEmpty()) return
                        var sum = 0.0
                        var i = 0
                        while (i + 1 < waveform.size) {
                            val sh = (waveform[i + 1].toInt() shl 8) or (waveform[i].toInt() and 0xFF)
                            val s = sh.toShort().toInt()
                            sum += s.toDouble() * s
                            i += 2
                        }
                        val rms = sqrt(sum / maxOf(1, waveform.size / 2))
                        val norm = min(1f, (rms / 4200f).toFloat())
                        onMouthNow(0.12f + 0.88f * norm)
                    }
                    override fun onFftDataCapture(
                        analyzer: Visualizer?,
                        fft: ByteArray?,
                        samplingRate: Int
                    ) {
                    }
                },
                maxOf(1000, Visualizer.getMaxCaptureRate() / 2),
                false,
                true
            )
            vis.setEnabled(true)
            visualizer = vis
        } catch (e: Exception) {
            startFlapFallback()
        }
    }

    private fun releaseVisualizer() {
        try {
            visualizer?.setEnabled(false)
            visualizer?.release()
        } catch (ignored: Exception) {}
        visualizer = null
    }

    private fun startFlapFallback() {
        flapRunnable = object : Runnable {
            private var frame = 0L
            override fun run() {
                if (stopped) return
                val wave = (Math.sin(frame * 0.55) * 0.5 + 0.5) * 0.55
                val jitter = Math.random() * 0.35
                onMouthNow(Math.min(1f, (0.15 + wave + jitter).toFloat()))
                frame++
                main.postDelayed(this, 60L)
            }
        }
        main.post(flapRunnable!!)
    }

    private fun onMouthNow(target: Float) {
        mouthNow += (target - mouthNow) * 0.35f
        val value = mouthNow.coerceIn(0f, 1f)
        main.post { onMouth(value) }
    }

    private fun setPlaying(playing: Boolean) {
        isPlaying = playing
        main.post { onSpeechChange?.invoke(playing) }
    }

    private fun releaseMedia() {
        flapRunnable?.let { main.removeCallbacks(it) }
        flapRunnable = null
        releaseVisualizer()
        try {
            player?.setOnCompletionListener(null)
            player?.setOnPreparedListener(null)
            player?.setOnErrorListener(null)
            player?.stop()
            player?.release()
        } catch (ignored: Exception) {}
        player = null
    }

    init {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                main.post { slotDone(utteranceId, true) }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                main.post { slotDone(utteranceId, false) }
            }

            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                main.post { slotDone(utteranceId, false) }
            }
        })
    }

    private fun slotDone(utteranceId: String?, success: Boolean) {
        val index = utteranceId?.removePrefix("mitra_")?.toIntOrNull() ?: return
        val job = jobs[index] ?: return
        if (success) {
            job.ready = true
        } else {
            job.file.delete()
            jobs.remove(index)
        }
        tickSlot()
    }
}