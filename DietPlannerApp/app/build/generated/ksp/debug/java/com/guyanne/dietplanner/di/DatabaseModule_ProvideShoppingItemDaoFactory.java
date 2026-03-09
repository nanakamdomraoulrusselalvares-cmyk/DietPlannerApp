package com.guyanne.dietplanner.di;

import com.guyanne.dietplanner.data.local.AppDatabase;
import com.guyanne.dietplanner.data.local.dao.ShoppingItemDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideShoppingItemDaoFactory implements Factory<ShoppingItemDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideShoppingItemDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ShoppingItemDao get() {
    return provideShoppingItemDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideShoppingItemDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideShoppingItemDaoFactory(databaseProvider);
  }

  public static ShoppingItemDao provideShoppingItemDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideShoppingItemDao(database));
  }
}
