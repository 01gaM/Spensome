package com.spensome.di

import android.content.Context
import com.spensome.data.ItemsRepository
import com.spensome.data.OfflineItemsRepository
import com.spensome.data.WishListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideItemsRepository(@ApplicationContext context: Context): ItemsRepository {
        return OfflineItemsRepository(WishListDatabase.getDatabase(context).itemDao())
    }
}
