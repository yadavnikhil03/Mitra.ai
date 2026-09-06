package com.mitra.app.ui.chat;

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
public final class ChatActivity_MembersInjector implements MembersInjector<ChatActivity> {
  private final Provider<FirebaseAuth> authProvider;

  public ChatActivity_MembersInjector(Provider<FirebaseAuth> authProvider) {
    this.authProvider = authProvider;
  }

  public static MembersInjector<ChatActivity> create(Provider<FirebaseAuth> authProvider) {
    return new ChatActivity_MembersInjector(authProvider);
  }

  @Override
  public void injectMembers(ChatActivity instance) {
    injectAuth(instance, authProvider.get());
  }

  @InjectedFieldSignature("com.mitra.app.ui.chat.ChatActivity.auth")
  public static void injectAuth(ChatActivity instance, FirebaseAuth auth) {
    instance.auth = auth;
  }
}
