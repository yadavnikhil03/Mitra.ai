package com.mitra.app.data.model;

import java.util.UUID;

/**
 * Roles that mirror the web app: "user" | "mitra"
 * (backend also accepts "mitra" directly — no translation needed)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/mitra/app/data/model/Role;", "", "(Ljava/lang/String;I)V", "USER", "MITRA", "app_debug"})
public enum Role {
    /*public static final*/ USER /* = new USER() */,
    /*public static final*/ MITRA /* = new MITRA() */;
    
    Role() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.mitra.app.data.model.Role> getEntries() {
        return null;
    }
}