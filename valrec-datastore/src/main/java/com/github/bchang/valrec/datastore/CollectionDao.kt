package com.github.bchang.valrec.datastore

import androidx.room.*

@Dao
internal interface CollectionDao {

    @Query("SELECT * FROM Collection WHERE uid = :collectionId")
    fun get(collectionId: Long): Collection

    @Query("SELECT COUNT(*) FROM Collection")
    fun getNumCollections(): Int

    @Query("SELECT * FROM Collection")
    fun getAll(): List<Collection>

    @Insert
    fun insert(collection: Collection): Long

    @Transaction
    fun ensureAtLeastOneCollection(): List<Collection> {
        if (getNumCollections() == 0) {
            insert(createCollection(""))
        }
        return getAll()
    }
}
