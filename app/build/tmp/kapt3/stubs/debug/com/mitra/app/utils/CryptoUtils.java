package com.mitra.app.utils;

import android.util.Base64;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fJ\u0016\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\fJ\u000e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\fJ\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\fH\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/mitra/app/utils/CryptoUtils;", "", "()V", "hasKey", "", "getHasKey", "()Z", "secretKey", "Ljavax/crypto/SecretKey;", "clearKey", "", "decrypt", "", "ciphertextB64", "uid", "encrypt", "plaintext", "initKey", "base64KeyBytes", "isLikelyBase64Blob", "text", "app_debug"})
public final class CryptoUtils {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile javax.crypto.SecretKey secretKey;
    
    @javax.inject.Inject()
    public CryptoUtils() {
        super();
    }
    
    public final boolean getHasKey() {
        return false;
    }
    
    public final void initKey(@org.jetbrains.annotations.NotNull()
    java.lang.String base64KeyBytes) {
    }
    
    public final void clearKey() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String encrypt(@org.jetbrains.annotations.NotNull()
    java.lang.String plaintext, @org.jetbrains.annotations.NotNull()
    java.lang.String uid) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String decrypt(@org.jetbrains.annotations.NotNull()
    java.lang.String ciphertextB64, @org.jetbrains.annotations.NotNull()
    java.lang.String uid) {
        return null;
    }
    
    private final boolean isLikelyBase64Blob(java.lang.String text) {
        return false;
    }
}