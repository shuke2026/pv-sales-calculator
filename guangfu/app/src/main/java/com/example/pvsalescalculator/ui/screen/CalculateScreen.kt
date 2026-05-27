package com.example.pvsalescalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pvsalescalculator.R
import com.example.pvsalescalculator.ui.components.DropdownField
import com.example.pvsalescalculator.ui.components.InputTextField
import com.example.pvsalescalculator.ui.components.ResultCard
import com.example.pvsalescalculator.ui.components.ResultRowCard
import com.example.pvsalescalculator.viewmodel.CalculateViewModel

@Composable
fun CalculateScreen(viewModel: CalculateViewModel) {
    val roofArea by viewModel.roofArea.collectAsState()
    val scenario by viewModel.scenario.collectAsState()
    val province by viewModel.province.collectAsState()
    val city by viewModel.city.collectAsState()
    val industryType by viewModel.industryType.collectAsState()
    val electricityType by viewModel.electricityType.collectAsState()
    val peakPrice by viewModel.peakPrice.collectAsState()
    val valleyPrice by viewModel.valleyPrice.collectAsState()
    val normalPrice by viewModel.normalPrice.collectAsState()
    val generationMode by viewModel.generationMode.collectAsState()
    val modulePower by viewModel.modulePower.collectAsState()
    val inclination by viewModel.inclination.collectAsState()
    val systemEfficiency by viewModel.systemEfficiency.collectAsState()
    val isProfessionalMode by viewModel.isProfessionalMode.collectAsState()
    val result by viewModel.result.collectAsState()
    val isCalculating by viewModel.isCalculating.collectAsState()

    val cities = if (province.isNotEmpty()) viewModel.getCities(province) else emptyList()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = if (isProfessionalMode) R.string.title_professional_calc else R.string.title_simple_calc),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 简易测算部分
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InputTextField(
                            label = stringResource(id = R.string.label_roof_area),
                            value = roofArea,
                            onValueChange = viewModel::updateRoofArea,
                            placeholder = "请输入面积",
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = R.string.label_scenario),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScenarioRadioButton(
                                label = "户用",
                                value = "residential",
                                selected = scenario == "residential",
                                onSelect = viewModel::updateScenario
                            )
                            ScenarioRadioButton(
                                label = "工商业",
                                value = "commercial",
                                selected = scenario == "commercial",
                                onSelect = viewModel::updateScenario
                            )
                            ScenarioRadioButton(
                                label = "工厂",
                                value = "factory",
                                selected = scenario == "factory",
                                onSelect = viewModel::updateScenario
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        DropdownField(
                            label = stringResource(id = R.string.label_province_city),
                            value = "$province $city",
                            options = if (province.isEmpty()) viewModel.provinces else cities,
                            onValueChange = {
                                if (province.isEmpty()) {
                                    viewModel.updateProvince(it)
                                } else {
                                    viewModel.updateCity(it)
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 专业测算部分
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Button(
                        onClick = viewModel::toggleProfessionalMode,
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isProfessionalMode) stringResource(id = R.string.btn_collapse) else stringResource(
                                    id = R.string.btn_expand_more
                                )
                            )
                            Icon(
                                imageVector = if (isProfessionalMode) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (isProfessionalMode) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            DropdownField(
                                label = stringResource(id = R.string.label_industry),
                                value = getIndustryLabel(industryType),
                                options = listOf("制造业", "化工", "食品加工", "金属加工", "纺织", "制药", "物流仓储", "商业零售"),
                                onValueChange = { viewModel.updateIndustryType(getIndustryValue(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            DropdownField(
                                label = stringResource(id = R.string.label_electricity_type),
                                value = getElectricityLabel(electricityType),
                                options = listOf("居民", "一般工商业", "大工业"),
                                onValueChange = { viewModel.updateElectricityType(getElectricityValue(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        label = stringResource(id = R.string.label_price_peak),
                                        value = peakPrice,
                                        onValueChange = viewModel::updatePeakPrice,
                                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                    )
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        label = stringResource(id = R.string.label_price_valley),
                                        value = valleyPrice,
                                        onValueChange = viewModel::updateValleyPrice,
                                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                    )
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        label = stringResource(id = R.string.label_price_normal),
                                        value = normalPrice,
                                        onValueChange = viewModel::updateNormalPrice,
                                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            DropdownField(
                                label = stringResource(id = R.string.label_generation_mode),
                                value = getGenerationModeLabel(generationMode),
                                options = listOf("全额上网", "自发自用", "余电上网"),
                                onValueChange = { viewModel.updateGenerationMode(getGenerationModeValue(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            DropdownField(
                                label = stringResource(id = R.string.label_module_power),
                                value = "$modulePower W",
                                options = listOf("450W", "500W", "550W"),
                                onValueChange = { viewModel.updateModulePower(it.replace("W", "").toInt()) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        label = stringResource(id = R.string.label_inclination),
                                        value = inclination,
                                        onValueChange = viewModel::updateInclination,
                                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                    )
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    InputTextField(
                                        label = stringResource(id = R.string.label_system_efficiency),
                                        value = systemEfficiency,
                                        onValueChange = viewModel::updateSystemEfficiency,
                                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 计算按钮
            item {
                Button(
                    onClick = viewModel::calculate,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCalculating && roofArea.isNotEmpty() && province.isNotEmpty() && city.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isCalculating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Calculate, contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = stringResource(id = R.string.btn_calculate))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 测算结果
            result?.let { res ->
                item {
                    Text(
                        text = "测算结果",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 关键指标卡片
                item {
                    androidx.compose.foundation.layout.Grid(
                        columns = 2,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ResultCard(
                            title = "装机容量",
                            value = String.format("%.2f", res.installedCapacity),
                            unit = "kW",
                            icon = R.drawable.ic_solar_panel,
                            bgColor = Color(0xFFE0F2FE)
                        )
                        ResultCard(
                            title = "年发电量",
                            value = String.format("%.0f", res.yearlyGeneration),
                            unit = "kWh",
                            icon = R.drawable.ic_electric,
                            bgColor = Color(0xFFECFDF5)
                        )
                        ResultCard(
                            title = "项目投资",
                            value = String.format("%.0f", res.totalInvestment),
                            unit = "元",
                            icon = R.drawable.ic_money,
                            bgColor = Color(0xFFFEF3C7)
                        )
                        ResultCard(
                            title = "年收益",
                            value = String.format("%.0f", res.yearlyProfit),
                            unit = "元",
                            icon = R.drawable.ic_trending_up,
                            bgColor = Color(0xFFD1FAE5)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 详细数据
                item {
                    ResultRowCard(
                        items = listOf(
                            Triple("组件数量", res.moduleCount.toString(), "块"),
                            Triple("日均发电", String.format("%.1f", res.dailyGeneration), "kWh"),
                            Triple("月均发电", String.format("%.0f", res.monthlyGeneration), "kWh"),
                            Triple("25年总发电", String.format("%.0f", res.total25YearGeneration), "kWh"),
                            Triple("月节电收益", String.format("%.0f", res.monthlyProfit), "元"),
                            Triple("25年总收益", String.format("%.0f", res.total25YearProfit), "元"),
                            Triple("静态回本周期", String.format("%.1f", res.staticPayback), "年"),
                            Triple("动态回本周期", String.format("%.1f", res.dynamicPayback), "年")
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 环保数据
                item {
                    ResultRowCard(
                        items = listOf(
                            Triple("用电覆盖比例", String.format("%.1f", res.coverageRatio), "%"),
                            Triple("节电率", String.format("%.1f", res.savingRate), "%"),
                            Triple("碳减排量", String.format("%.0f", res.carbonReduction), "吨CO₂"),
                            Triple("等效植树", res.treeEquivalent.toString(), "棵")
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 操作按钮
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = { viewModel.saveRecord() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = stringResource(id = R.string.btn_save_record))
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            onClick = { /* 生成报告 */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = stringResource(id = R.string.btn_generate_report))
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ScenarioRadioButton(
    label: String,
    value: String,
    selected: Boolean,
    onSelect: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = { onSelect(value) },
            colors = androidx.compose.material3.RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

fun getIndustryLabel(type: String): String {
    return when (type) {
        "manufacturing" -> "制造业"
        "chemical" -> "化工"
        "food" -> "食品加工"
        "metal" -> "金属加工"
        "textile" -> "纺织"
        "pharmaceutical" -> "制药"
        "logistics" -> "物流仓储"
        "retail" -> "商业零售"
        else -> type
    }
}

fun getIndustryValue(label: String): String {
    return when (label) {
        "制造业" -> "manufacturing"
        "化工" -> "chemical"
        "食品加工" -> "food"
        "金属加工" -> "metal"
        "纺织" -> "textile"
        "制药" -> "pharmaceutical"
        "物流仓储" -> "logistics"
        "商业零售" -> "retail"
        else -> "manufacturing"
    }
}

fun getElectricityLabel(type: String): String {
    return when (type) {
        "residential" -> "居民"
        "commercial" -> "一般工商业"
        "industrial" -> "大工业"
        else -> type
    }
}

fun getElectricityValue(label: String): String {
    return when (label) {
        "居民" -> "residential"
        "一般工商业" -> "commercial"
        "大工业" -> "industrial"
        else -> "commercial"
    }
}

fun getGenerationModeLabel(mode: String): String {
    return when (mode) {
        "full_feed" -> "全额上网"
        "self_use" -> "自发自用"
        "excess_feed" -> "余电上网"
        else -> mode
    }
}

fun getGenerationModeValue(label: String): String {
    return when (label) {
        "全额上网" -> "full_feed"
        "自发自用" -> "self_use"
        "余电上网" -> "excess_feed"
        else -> "self_use"
    }
}