package com.mitra.app.ui.chat;

import android.content.Context;
import com.mitra.app.data.api.MitraApiService;
import com.mitra.app.data.repository.AuthRepository;
import com.mitra.app.data.repository.ChatRepository;
import com.mitra.app.utils.CryptoUtils;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<Context> appContextProvider;

  private final Provider<MitraApiService> apiServiceProvider;

  private final Provider<AuthRepository> authRepoProvider;

  private final Provider<ChatRepository> chatRepoProvider;

  private final Provider<CryptoUtils> cryptoUtilsProvider;

  public ChatViewModel_Factory(Provider<Context> appContextProvider,
      Provider<MitraApiService> apiServiceProvider, Provider<AuthRepository> authRepoProvider,
      Provider<ChatRepository> chatRepoProvider, Provider<CryptoUtils> cryptoUtilsProvider) {
    this.appContextProvider = appContextProvider;
    this.apiServiceProvider = apiServiceProvider;
    this.authRepoProvider = authRepoProvider;
    this.chatRepoProvider = chatRepoProvider;
    this.cryptoUtilsProvider = cryptoUtilsProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(appContextProvider.get(), apiServiceProvider.get(), authRepoProvider.get(), chatRepoProvider.get(), cryptoUtilsProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<Context> appContextProvider,
      Provider<MitraApiService> apiServiceProvider, Provider<AuthRepository> authRepoProvider,
      Provider<ChatRepository> chatRepoProvider, Provider<CryptoUtils> cryptoUtilsProvider) {
    return new ChatViewModel_Factory(appContextProvider, apiServiceProvider, authRepoProvider, chatRepoProvider, cryptoUtilsProvider);
  }

  public static ChatViewModel newInstance(Context appContext, MitraApiService apiService,
      AuthRepository authRepo, ChatRepository chatRepo, CryptoUtils cryptoUtils) {
    return new ChatViewModel(appContext, apiService, authRepo, chatRepo, cryptoUtils);
  }
}
