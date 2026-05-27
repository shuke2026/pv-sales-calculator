package com.example.pvsalescalculator.data.repository

import com.example.pvsalescalculator.data.database.AppDatabase
import com.example.pvsalescalculator.data.database.entity.CalculationRecord
import com.example.pvsalescalculator.data.database.entity.Client

class LocalRepository(private val database: AppDatabase) {
    // Client operations
    suspend fun insertClient(client: Client): Long {
        return database.clientDao().insert(client)
    }

    suspend fun updateClient(client: Client) {
        database.clientDao().update(client)
    }

    suspend fun deleteClient(client: Client) {
        database.clientDao().delete(client)
    }

    fun getAllClients() = database.clientDao().getAllClients()

    suspend fun getClientById(id: Long) = database.clientDao().getClientById(id)

    fun searchClients(query: String) = database.clientDao().searchClients("%$query%")

    // Calculation record operations
    suspend fun insertRecord(record: CalculationRecord): Long {
        return database.calculationRecordDao().insert(record)
    }

    suspend fun updateRecord(record: CalculationRecord) {
        database.calculationRecordDao().update(record)
    }

    suspend fun deleteRecord(record: CalculationRecord) {
        database.calculationRecordDao().delete(record)
    }

    fun getAllRecords() = database.calculationRecordDao().getAllRecords()

    fun getRecordsByClient(clientId: Long) = database.calculationRecordDao().getRecordsByClient(clientId)

    suspend fun getRecordById(id: Long) = database.calculationRecordDao().getRecordById(id)
}