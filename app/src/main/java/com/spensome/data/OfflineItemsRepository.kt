package com.spensome.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemById(id: Int): Flow<Item> = itemDao.getItemById(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)
}
