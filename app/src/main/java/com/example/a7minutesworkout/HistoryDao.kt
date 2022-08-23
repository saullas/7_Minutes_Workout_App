package com.example.a7minutesworkout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("select * from history_table")
    fun getAll() : Flow<List<HistoryEntity>>

//    @Query("select * from history_table where id=:id")
//    fun getOne(id : Int) :
}