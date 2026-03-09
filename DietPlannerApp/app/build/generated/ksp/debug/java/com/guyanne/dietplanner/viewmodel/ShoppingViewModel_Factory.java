package com.guyanne.dietplanner.viewmodel;

import com.guyanne.dietplanner.data.repository.DietRepository;
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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ShoppingViewModel_Factory implements Factory<ShoppingViewModel> {
  private final Provider<DietRepository> repositoryProvider;

  public ShoppingViewModel_Factory(Provider<DietRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ShoppingViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ShoppingViewModel_Factory create(Provider<DietRepository> repositoryProvider) {
    return new ShoppingViewModel_Factory(repositoryProvider);
  }

  public static ShoppingViewModel newInstance(DietRepository repository) {
    return new ShoppingViewModel(repository);
  }
}
