package com.example.pvsalescalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pvsalescalculator.data.database.entity.CalculationRecord
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportsViewModel(
    private val repository: com.example.pvsalescalculator.data.repository.LocalRepository
) : ViewModel() {

    val records: StateFlow<List<CalculationRecord>> = repository.getAllRecords()

    fun deleteRecord(record: CalculationRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

    suspend fun getRecordById(id: Long): CalculationRecord? {
        return repository.getRecordById(id)
    }
}