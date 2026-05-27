package com.example.pvsalescalculator.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Dialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pvsalescalculator.R
import com.example.pvsalescalculator.data.database.entity.Client
import com.example.pvsalescalculator.viewmodel.ClientsViewModel

@Composable
fun ClientsScreen(viewModel: ClientsViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val clients by viewModel.clients.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var editingClient by remember { mutableStateOf<Client?>(null) }

    androidx.compose.material3.Scaffold(
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = {
                    editingClient = null
                    showAddDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // 搜索框
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                androidx.compose.material3.SearchBar(
                    query = searchQuery,
                    onQueryChange = viewModel::updateSearchQuery,
                    onSearch = {},
                    placeholder = { Text(text = stringResource(id = R.string.placeholder_search_client)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                ) {}
            }

            // 客户列表
            if (clients.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.label_no_reports),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                ) {
                    items(clients) { client ->
                        ClientCard(
                            client = client,
                            onEdit = { editingClient = client; showAddDialog = true },
                            onDelete = { viewModel.deleteClient(client) }
                        )
                    }
                }
            }
        }
    }

    // 添加/编辑对话框
    if (showAddDialog) {
        ClientDialog(
            client = editingClient,
            onDismiss = { showAddDialog = false; editingClient = null },
            onSave = { name, phone, address, remark ->
                if (editingClient != null) {
                    viewModel.updateClient(editingClient.copy(
                        name = name,
                        phone = phone,
                        address = address,
                        remark = remark
                    ))
                } else {
                    viewModel.addClient(name, phone, address, remark)
                }
                showAddDialog = false
                editingClient = null
            }
        )
    }
}

@Composable
fun ClientCard(
    client: Client,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            if (client.phone.isNotEmpty()) {
                Text(
                    text = client.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            if (client.address.isNotEmpty()) {
                Text(
                    text = client.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDialog(
    client: Client?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(client?.name ?: "") }
    var phone by remember { mutableStateOf(client?.phone ?: "") }
    var address by remember { mutableStateOf(client?.address ?: "") }
    var remark by remember { mutableStateOf(client?.remark ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (client != null) "编辑客户" else "添加客户",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(id = R.string.label_client_name)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(text = stringResource(id = R.string.label_client_phone)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(text = stringResource(id = R.string.label_client_address)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = remark,
                    onValueChange = { remark = it },
                    label = { Text(text = stringResource(id = R.string.label_client_remark)) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "取消")
                    }
                    Button(
                        onClick = { onSave(name, phone, address, remark) },
                        enabled = name.isNotEmpty()
                    ) {
                        Text(text = "保存")
                    }
                }
            }
        }
    }
}