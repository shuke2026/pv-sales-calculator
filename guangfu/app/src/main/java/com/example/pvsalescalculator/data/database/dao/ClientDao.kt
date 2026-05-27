package com.example.pvsalescalculator.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pvsalescalculator.data.database.entity.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert
    suspend fun insert(client: Client): Long

    @Update
    suspend fun update(client: Client)

    @Delete
    suspend fun delete(client: Client)

    @Query("SELECT * FROM clients ORDER BY createdAt DESC")
    fun getAllClients(): Flow<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: Long): Client?

    @Query("SELECT * FROM clients WHERE name LIKE :query OR phone LIKE :query ORDER BY createdAt DESC")
    fun searchClients(query: String): Flow<List<Client>>
}