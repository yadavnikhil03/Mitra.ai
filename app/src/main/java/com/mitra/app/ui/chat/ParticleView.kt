package com.mitra.app.ui.chat

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.Choreographer
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
class ParticleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val EMBER_CORE  = 0xFFFFD3A0.toInt()
    private val EMBER       = 0xFFFFB877.toInt()
    private val EMBER_DEEP  = 0xFFF0925A.toInt()
    private val ROSE        = 0xFFE89BA8.toInt()
    private val ALL_COLOURS = intArrayOf(EMBER_CORE, EMBER, EMBER_DEEP)

    private val FOV   = 320f
    private val DEPTH = 900f

    private data class Particle(
        var x: Float, var y: Float, var z: Float,
        val r: Float,
        val v: Float,
        var sway: Float,
        val swaySpeed: Float,
        var tw: Float,
        val colour: Int
    )

    private val particles   = mutableListOf<Particle>()
    private val paint       = Paint(Paint.ANTI_ALIAS_FLAG)
    private var running     = false
    private var cx          = 0f
    private var cy          = 0f

    private val choreographer = Choreographer.getInstance()
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (!running) return
            tick()
            invalidate()
            choreographer.postFrameCallback(this)
        }
    }

    private val spriteCache = mutableMapOf<Int, RadialGradient>()
    private var spriteRadius = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cx = w / 2f
        cy = h * 0.42f
        spriteRadius = w * 0.04f
        spriteCache.clear()
        ALL_COLOURS.forEach { col -> buildSprite(col, spriteRadius) }
        rebuildParticles(w, h)
    }

    private fun buildSprite(colour: Int, radius: Float): RadialGradient {
        val r = (colour shr 16 and 0xFF) / 255f
        val g = (colour shr 8  and 0xFF) / 255f
        val b = (colour        and 0xFF) / 255f
        val grad = RadialGradient(
            0f, 0f, radius,
            intArrayOf(colour, adjustAlpha(colour, 0.45f), adjustAlpha(colour, 0f)),
            floatArrayOf(0f, 0.35f, 1f),
            Shader.TileMode.CLAMP
        )
        return grad.also { spriteCache[colour] = it }
    }

    private fun adjustAlpha(colour: Int, alpha: Float): Int {
        val a = (alpha * 255).toInt().coerceIn(0, 255)
        return (colour and 0x00FFFFFF) or (a shl 24)
    }

    private fun rebuildParticles(w: Int, h: Int) {
        particles.clear()
        val count = min(90, (w * h) / 14000)
        repeat(count) { particles.add(newParticle(w.toFloat(), h.toFloat())) }
    }

    private fun newParticle(w: Float, h: Float): Particle {
        val pickColour = if (Math.random() < 0.12) ROSE
                         else ALL_COLOURS[( Math.random() * 3).toInt()]
        return Particle(
            x         = ((Math.random() * 2 - 1) * w * 0.7).toFloat(),
            y         = ((Math.random() * 2 - 1) * h * 0.7).toFloat(),
            z         = (Math.random() * DEPTH).toFloat(),
            r         = (1.4f + (Math.random() * 2.6).toFloat()),
            v         = (0.12f + (Math.random() * 0.4).toFloat()),
            sway      = (Math.random() * Math.PI * 2).toFloat(),
            swaySpeed = (0.002f + (Math.random() * 0.004).toFloat()),
            tw        = (Math.random() * Math.PI * 2).toFloat(),
            colour    = pickColour
        )
    }

    private fun tick() {
        val w = width.toFloat()
        val h = height.toFloat()
        val iter = particles.listIterator()
        while (iter.hasNext()) {
            val p = iter.next()
            p.z    -= p.v
            p.sway += p.swaySpeed
            p.tw   += 0.02f
            if (p.z <= 1f) {
                iter.set(newParticle(w, h).also { it.z = DEPTH })
                continue
            }
            val scale = FOV / (FOV + p.z)
            val sx = cx + (p.x + sin(p.sway) * 26) * scale
            val sy = cy + (p.y + cos(p.sway * 0.8f) * 18) * scale
            if (sx < -40 || sx > w + 40 || sy < -40 || sy > h + 40) continue
            p.x = p.x  // positions unchanged, recalculated each frame
        }
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        for (p in particles) {
            if (p.z <= 1f) continue
            val scale  = FOV / (FOV + p.z)
            val sx     = cx + (p.x + sin(p.sway) * 26) * scale
            val sy     = cy + (p.y + cos(p.sway * 0.8f) * 18) * scale
            if (sx < -40 || sx > w + 40 || sy < -40 || sy > h + 40) continue

            val size   = p.r * scale * 7f
            val alphaF = min(0.85f, scale * 1.1f) * (0.7f + 0.3f * sin(p.tw))

            paint.color = applyAlpha(p.colour, alphaF)
            canvas.drawCircle(sx, sy, size, paint)
        }
    }

    private fun applyAlpha(colour: Int, alpha: Float): Int {
        val a = (alpha * 255).toInt().coerceIn(0, 255)
        return (colour and 0x00FFFFFF) or (a shl 24)
    }

    fun start() {
        if (running) return
        running = true
        choreographer.postFrameCallback(frameCallback)
    }

    fun stop() {
        running = false
        choreographer.removeFrameCallback(frameCallback)
    }

    override fun onDetachedFromWindow() {
        stop()
        super.onDetachedFromWindow()
    }
}
