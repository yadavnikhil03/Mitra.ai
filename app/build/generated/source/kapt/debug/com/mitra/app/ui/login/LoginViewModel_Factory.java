package com.mitra.app.ui.login;

import com.mitra.app.data.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<AuthRepository> authRepoProvider;

  public LoginViewModel_Factory(Provider<AuthRepository> authRepoProvider) {
    this.authRepoProvider = authRepoProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(authRepoProvider.get());
  }

  public static LoginViewModel_Factory create(Provider<AuthRepository> authRepoProvider) {
    return new LoginViewModel_Factory(authRepoProvider);
  }

  public static LoginViewModel newInstance(AuthRepository authRepo) {
    return new LoginViewModel(authRepo);
  }
}
