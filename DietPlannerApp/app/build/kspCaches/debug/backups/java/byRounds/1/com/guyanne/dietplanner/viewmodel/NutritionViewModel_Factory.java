package com.guyanne.dietplanner.viewmodel;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
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
public final class NutritionViewModel_Factory implements Factory<NutritionViewModel> {
  private final Provider<DietRepository> repositoryProvider;

  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public NutritionViewModel_Factory(Provider<DietRepository> repositoryProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.repositoryProvider = repositoryProvider;
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public NutritionViewModel get() {
    return newInstance(repositoryProvider.get(), dataStoreProvider.get());
  }

  public static NutritionViewModel_Factory create(Provider<DietRepository> repositoryProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new NutritionViewModel_Factory(repositoryProvider, dataStoreProvider);
  }

  public static NutritionViewModel newInstance(DietRepository repository,
      DataStore<Preferences> dataStore) {
    return new NutritionViewModel(repository, dataStore);
  }
}
