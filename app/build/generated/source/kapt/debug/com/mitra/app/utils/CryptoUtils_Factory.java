package com.mitra.app.utils;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class CryptoUtils_Factory implements Factory<CryptoUtils> {
  @Override
  public CryptoUtils get() {
    return newInstance();
  }

  public static CryptoUtils_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CryptoUtils newInstance() {
    return new CryptoUtils();
  }

  private static final class InstanceHolder {
    private static final CryptoUtils_Factory INSTANCE = new CryptoUtils_Factory();
  }
}
