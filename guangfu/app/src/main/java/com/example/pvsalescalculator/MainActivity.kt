package com.example.pvsalescalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.pvsalescalculator.data.database.AppDatabase
import com.example.pvsalescalculator.data.repository.LocalRepository
import com.example.pvsalescalculator.ui.screen.CalculateScreen
import com.example.pvsalescalculator.ui.screen.ClientsScreen
import com.example.pvsalescalculator.ui.screen.ProfileScreen
import com.example.pvsalescalculator.ui.screen.ReportsScreen
import com.example.pvsalescalculator.viewmodel.CalculateViewModel
import com.example.pvsalescalculator.viewmodel.ClientsViewModel
import com.example.pvsalescalculator.viewmodel.ReportsViewModel

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var repository: LocalRepository
    private lateinit var calculateViewModel: CalculateViewModel
    private lateinit var clientsViewModel: ClientsViewModel
    private lateinit var reportsViewModel: ReportsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化数据库
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "pv_sales_calculator"
        ).build()

        repository = LocalRepository(database)

        // 初始化 ViewModel
        calculateViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return CalculateViewModel(repository) as T
            }
        })[CalculateViewModel::class.java]

        clientsViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ClientsViewModel(repository) as T
            }
        })[ClientsViewModel::class.java]

        reportsViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ReportsViewModel(repository) as T
            }
        })[ReportsViewModel::class.java]

        setContent {
            PVApp(
                calculateViewModel = calculateViewModel,
                clientsViewModel = clientsViewModel,
                reportsViewModel = reportsViewModel
            )
        }
    }
}

@Composable
fun PVApp(
    calculateViewModel: CalculateViewModel,
    clientsViewModel: ClientsViewModel,
    reportsViewModel: ReportsViewModel
) {
    val navItems = listOf(
        NavigationItem.Calculate,
        NavigationItem.Clients,
        NavigationItem.Reports,
        NavigationItem.Profile
    )
    var currentNavIndex by androidx.compose.runtime.mutableStateOf(0)

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    androidx.compose.material3.NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        navItems.forEachIndexed { index, item ->
                            androidx.compose.material3.NavigationBarItem(
                                icon = {
                                    androidx.compose.material3.Icon(
                                        imageVector = if (currentNavIndex == index) item.selectedIcon else item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(text = item.label) },
                                selected = currentNavIndex == index,
                                onClick = { currentNavIndex = index },
                                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }
                }
            ) { padding ->
                androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    when (navItems[currentNavIndex]) {
                        NavigationItem.Calculate -> CalculateScreen(viewModel = calculateViewModel)
                        NavigationItem.Clients -> ClientsScreen(viewModel = clientsViewModel)
                        NavigationItem.Reports -> ReportsScreen(viewModel = reportsViewModel)
                        NavigationItem.Profile -> ProfileScreen()
                    }
                }
            }
        }
    }
}

sealed class NavigationItem(
    val icon: androidx.compose.material.icons.IconVector,
    val selectedIcon: androidx.compose.material.icons.IconVector,
    val label: String
) {
    object Calculate : NavigationItem(
        icon = androidx.compose.material.icons.filled.Calculate,
        selectedIcon = androidx.compose.material.icons.filled.Calculate,
        label = "测算"
    )
    object Clients : NavigationItem(
        icon = androidx.compose.material.icons.filled.Users,
        selectedIcon = androidx.compose.material.icons.filled.Users,
        label = "客户"
    )
    object Reports : NavigationItem(
        icon = androidx.compose.material.icons.filled.PictureAsPdf,
        selectedIcon = androidx.compose.material.icons.filled.PictureAsPdf,
        label = "报告"
    )
    object Profile : NavigationItem(
        icon = androidx.compose.material.icons.filled.Person,
        selectedIcon = androidx.compose.material.icons.filled.Person,
        label = "我的"
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Text(text = "Hello World!")
    }
}