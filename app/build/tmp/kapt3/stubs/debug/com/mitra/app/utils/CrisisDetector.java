package com.mitra.app.utils;

/**
 * Port of the web app's CRISIS_PHRASES + CRISIS_REGEX lists.
 * Kept as a pure utility object — no Android deps — for easy unit-testing.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/mitra/app/utils/CrisisDetector;", "", "()V", "PHRASES", "", "", "REGEX_PATTERNS", "Lkotlin/text/Regex;", "isCrisis", "", "text", "app_debug"})
public final class CrisisDetector {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> PHRASES = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<kotlin.text.Regex> REGEX_PATTERNS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.mitra.app.utils.CrisisDetector INSTANCE = null;
    
    private CrisisDetector() {
        super();
    }
    
    public final boolean isCrisis(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
}