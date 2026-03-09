package com.guyanne.dietplanner.data.repository;

import com.guyanne.dietplanner.data.local.dao.MealDao;
import com.guyanne.dietplanner.data.local.dao.ShoppingItemDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DietRepository_Factory implements Factory<DietRepository> {
  private final Provider<MealDao> mealDaoProvider;

  private final Provider<ShoppingItemDao> shoppingItemDaoProvider;

  public DietRepository_Factory(Provider<MealDao> mealDaoProvider,
      Provider<ShoppingItemDao> shoppingItemDaoProvider) {
    this.mealDaoProvider = mealDaoProvider;
    this.shoppingItemDaoProvider = shoppingItemDaoProvider;
  }

  @Override
  public DietRepository get() {
    return newInstance(mealDaoProvider.get(), shoppingItemDaoProvider.get());
  }

  public static DietRepository_Factory create(Provider<MealDao> mealDaoProvider,
      Provider<ShoppingItemDao> shoppingItemDaoProvider) {
    return new DietRepository_Factory(mealDaoProvider, shoppingItemDaoProvider);
  }

  public static DietRepository newInstance(MealDao mealDao, ShoppingItemDao shoppingItemDao) {
    return new DietRepository(mealDao, shoppingItemDao);
  }
}
