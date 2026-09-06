package com.mitra.app.ui.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.View;

/**
 * Full-screen particle canvas that reproduces the web app's 2-D starfield.
 *
 * Particles float toward the camera (z-depth projection), swaying gently,
 * with warm ember/rose colours — matching the CSS canvas animation exactly.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u00018B\u001b\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010#\u001a\u00020\f2\u0006\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020\nH\u0002J\u0018\u0010&\u001a\u00020\f2\u0006\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020\nH\u0002J\u0018\u0010\'\u001a\u00020!2\u0006\u0010$\u001a\u00020\f2\u0006\u0010(\u001a\u00020\nH\u0002J\u0018\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\nH\u0002J\b\u0010,\u001a\u00020-H\u0014J\u0010\u0010.\u001a\u00020-2\u0006\u0010/\u001a\u000200H\u0014J(\u00101\u001a\u00020-2\u0006\u0010*\u001a\u00020\f2\u0006\u0010+\u001a\u00020\f2\u0006\u00102\u001a\u00020\f2\u0006\u00103\u001a\u00020\fH\u0014J\u0018\u00104\u001a\u00020-2\u0006\u0010*\u001a\u00020\f2\u0006\u0010+\u001a\u00020\fH\u0002J\u0006\u00105\u001a\u00020-J\u0006\u00106\u001a\u00020-J\b\u00107\u001a\u00020-H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n \u0013*\u0004\u0018\u00010\u00120\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020!0 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00069"}, d2 = {"Lcom/mitra/app/ui/chat/ParticleView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "ALL_COLOURS", "", "DEPTH", "", "EMBER", "", "EMBER_CORE", "EMBER_DEEP", "FOV", "ROSE", "choreographer", "Landroid/view/Choreographer;", "kotlin.jvm.PlatformType", "cx", "cy", "frameCallback", "Landroid/view/Choreographer$FrameCallback;", "paint", "Landroid/graphics/Paint;", "particles", "", "Lcom/mitra/app/ui/chat/ParticleView$Particle;", "running", "", "spriteCache", "", "Landroid/graphics/RadialGradient;", "spriteRadius", "adjustAlpha", "colour", "alpha", "applyAlpha", "buildSprite", "radius", "newParticle", "w", "h", "onDetachedFromWindow", "", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onSizeChanged", "oldw", "oldh", "rebuildParticles", "start", "stop", "tick", "Particle", "app_debug"})
public final class ParticleView extends android.view.View {
    private final int EMBER_CORE = -11360;
    private final int EMBER = -18313;
    private final int EMBER_DEEP = -1011110;
    private final int ROSE = -1533016;
    @org.jetbrains.annotations.NotNull()
    private final int[] ALL_COLOURS = null;
    private final float FOV = 320.0F;
    private final float DEPTH = 900.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.mitra.app.ui.chat.ParticleView.Particle> particles = null;
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint paint = null;
    private boolean running = false;
    private float cx = 0.0F;
    private float cy = 0.0F;
    private final android.view.Choreographer choreographer = null;
    @org.jetbrains.annotations.NotNull()
    private final android.view.Choreographer.FrameCallback frameCallback = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Integer, android.graphics.RadialGradient> spriteCache = null;
    private float spriteRadius = 0.0F;
    
    @kotlin.jvm.JvmOverloads()
    public ParticleView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @java.lang.Override()
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    }
    
    private final android.graphics.RadialGradient buildSprite(int colour, float radius) {
        return null;
    }
    
    private final int adjustAlpha(int colour, float alpha) {
        return 0;
    }
    
    private final void rebuildParticles(int w, int h) {
    }
    
    private final com.mitra.app.ui.chat.ParticleView.Particle newParticle(float w, float h) {
        return null;
    }
    
    private final void tick() {
    }
    
    @java.lang.Override()
    protected void onDraw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    private final int applyAlpha(int colour, float alpha) {
        return 0;
    }
    
    public final void start() {
    }
    
    public final void stop() {
    }
    
    @java.lang.Override()
    protected void onDetachedFromWindow() {
    }
    
    @kotlin.jvm.JvmOverloads()
    public ParticleView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u001d\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\fH\u00c6\u0003Jc\u0010(\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020\fH\u00d6\u0001J\t\u0010-\u001a\u00020.H\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0011\"\u0004\b\u0013\u0010\u0014R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u001a\u0010\n\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0011\"\u0004\b\u0017\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0011\"\u0004\b\u001a\u0010\u0014R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0011\"\u0004\b\u001c\u0010\u0014R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0011\"\u0004\b\u001e\u0010\u0014\u00a8\u0006/"}, d2 = {"Lcom/mitra/app/ui/chat/ParticleView$Particle;", "", "x", "", "y", "z", "r", "v", "sway", "swaySpeed", "tw", "colour", "", "(FFFFFFFFI)V", "getColour", "()I", "getR", "()F", "getSway", "setSway", "(F)V", "getSwaySpeed", "getTw", "setTw", "getV", "getX", "setX", "getY", "setY", "getZ", "setZ", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    static final class Particle {
        private float x;
        private float y;
        private float z;
        private final float r = 0.0F;
        private final float v = 0.0F;
        private float sway;
        private final float swaySpeed = 0.0F;
        private float tw;
        private final int colour = 0;
        
        public Particle(float x, float y, float z, float r, float v, float sway, float swaySpeed, float tw, int colour) {
            super();
        }
        
        public final float getX() {
            return 0.0F;
        }
        
        public final void setX(float p0) {
        }
        
        public final float getY() {
            return 0.0F;
        }
        
        public final void setY(float p0) {
        }
        
        public final float getZ() {
            return 0.0F;
        }
        
        public final void setZ(float p0) {
        }
        
        public final float getR() {
            return 0.0F;
        }
        
        public final float getV() {
            return 0.0F;
        }
        
        public final float getSway() {
            return 0.0F;
        }
        
        public final void setSway(float p0) {
        }
        
        public final float getSwaySpeed() {
            return 0.0F;
        }
        
        public final float getTw() {
            return 0.0F;
        }
        
        public final void setTw(float p0) {
        }
        
        public final int getColour() {
            return 0;
        }
        
        public final float component1() {
            return 0.0F;
        }
        
        public final float component2() {
            return 0.0F;
        }
        
        public final float component3() {
            return 0.0F;
        }
        
        public final float component4() {
            return 0.0F;
        }
        
        public final float component5() {
            return 0.0F;
        }
        
        public final float component6() {
            return 0.0F;
        }
        
        public final float component7() {
            return 0.0F;
        }
        
        public final float component8() {
            return 0.0F;
        }
        
        public final int component9() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mitra.app.ui.chat.ParticleView.Particle copy(float x, float y, float z, float r, float v, float sway, float swaySpeed, float tw, int colour) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}