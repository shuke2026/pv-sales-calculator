package com.example.pvsalescalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pvsalescalculator.data.database.entity.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ClientsViewModel(
    private val repository: com.example.pvsalescalculator.data.repository.LocalRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allClients = repository.getAllClients()
    private val _searchResults = repository.searchClients("")

    val clients = combine(_searchQuery, _allClients, _searchResults) { query, all, search ->
        if (query.isBlank()) all else search
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addClient(name: String, phone: String, address: String, remark: String) {
        viewModelScope.launch {
            val client = Client(
                name = name,
                phone = phone,
                address = address,
                remark = remark
            )
            repository.insertClient(client)
        }
    }

    fun updateClient(client: Client) {
        viewModelScope.launch {
            repository.updateClient(client)
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    suspend fun getClientById(id: Long): Client? {
        return repository.getClientById(id)
    }
}