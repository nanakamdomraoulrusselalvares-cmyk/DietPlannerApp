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
public final class MealPlanViewModel_Factory implements Factory<MealPlanViewModel> {
  private final Provider<DietRepository> repositoryProvider;

  public MealPlanViewModel_Factory(Provider<DietRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MealPlanViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static MealPlanViewModel_Factory create(Provider<DietRepository> repositoryProvider) {
    return new MealPlanViewModel_Factory(repositoryProvider);
  }

  public static MealPlanViewModel newInstance(DietRepository repository) {
    return new MealPlanViewModel(repository);
  }
}
