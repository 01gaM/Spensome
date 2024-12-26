package com.spensome.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Item::class], exportSchema = false)
abstract class WishListDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: WishListDatabase? = null

        fun getDatabase(context: Context): WishListDatabase {
            return Instance ?: synchronized(context) {
                Room.databaseBuilder(
                    context,
                    WishListDatabase::class.java,
                    "wishlist_database"
                ).fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}
