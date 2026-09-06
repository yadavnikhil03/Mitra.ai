package com.mitra.app.ui.splash;

import com.google.firebase.auth.FirebaseAuth;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SplashActivity_MembersInjector implements MembersInjector<SplashActivity> {
  private final Provider<FirebaseAuth> authProvider;

  public SplashActivity_MembersInjector(Provider<FirebaseAuth> authProvider) {
    this.authProvider = authProvider;
  }

  public static MembersInjector<SplashActivity> create(Provider<FirebaseAuth> authProvider) {
    return new SplashActivity_MembersInjector(authProvider);
  }

  @Override
  public void injectMembers(SplashActivity instance) {
    injectAuth(instance, authProvider.get());
  }

  @InjectedFieldSignature("com.mitra.app.ui.splash.SplashActivity.auth")
  public static void injectAuth(SplashActivity instance, FirebaseAuth auth) {
    instance.auth = auth;
  }
}
