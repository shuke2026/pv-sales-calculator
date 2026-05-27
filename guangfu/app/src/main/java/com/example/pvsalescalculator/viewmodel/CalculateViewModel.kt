package com.example.pvsalescalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pvsalescalculator.calculator.CalculationParams
import com.example.pvsalescalculator.calculator.CalculationResult
import com.example.pvsalescalculator.calculator.PVCalculator
import com.example.pvsalescalculator.data.database.entity.CalculationRecord
import com.example.pvsalescalculator.data.mock.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculateViewModel(
    private val repository: com.example.pvsalescalculator.data.repository.LocalRepository
) : ViewModel() {

    private val pvCalculator = PVCalculator()

    // UI State
    private val _roofArea = MutableStateFlow("")
    val roofArea: StateFlow<String> = _roofArea.asStateFlow()

    private val _scenario = MutableStateFlow("residential")
    val scenario: StateFlow<String> = _scenario.asStateFlow()

    private val _province = MutableStateFlow("")
    val province: StateFlow<String> = _province.asStateFlow()

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city.asStateFlow()

    private val _industryType = MutableStateFlow("manufacturing")
    val industryType: StateFlow<String> = _industryType.asStateFlow()

    private val _electricityType = MutableStateFlow("commercial")
    val electricityType: StateFlow<String> = _electricityType.asStateFlow()

    private val _peakPrice = MutableStateFlow("")
    val peakPrice: StateFlow<String> = _peakPrice.asStateFlow()

    private val _valleyPrice = MutableStateFlow("")
    val valleyPrice: StateFlow<String> = _valleyPrice.asStateFlow()

    private val _normalPrice = MutableStateFlow("")
    val normalPrice: StateFlow<String> = _normalPrice.asStateFlow()

    private val _generationMode = MutableStateFlow("self_use")
    val generationMode: StateFlow<String> = _generationMode.asStateFlow()

    private val _modulePower = MutableStateFlow(500)
    val modulePower: StateFlow<Int> = _modulePower.asStateFlow()

    private val _inclination = MutableStateFlow("30")
    val inclination: StateFlow<String> = _inclination.asStateFlow()

    private val _systemEfficiency = MutableStateFlow("82")
    val systemEfficiency: StateFlow<String> = _systemEfficiency.asStateFlow()

    private val _isProfessionalMode = MutableStateFlow(false)
    val isProfessionalMode: StateFlow<Boolean> = _isProfessionalMode.asStateFlow()

    private val _result = MutableStateFlow<CalculationResult?>(null)
    val result: StateFlow<CalculationResult?> = _result.asStateFlow()

    private val _isCalculating = MutableStateFlow(false)
    val isCalculating: StateFlow<Boolean> = _isCalculating.asStateFlow()

    private val _savedRecordId = MutableStateFlow<Long?>(null)
    val savedRecordId: StateFlow<Long?> = _savedRecordId.asStateFlow()

    // 数据列表
    val provinces = MockData.getProvinces()

    fun getCities(province: String): List<String> {
        return MockData.getCitiesByProvince(province)
    }

    // 更新方法
    fun updateRoofArea(value: String) {
        _roofArea.value = value
    }

    fun updateScenario(value: String) {
        _scenario.value = value
        updateDefaultPrices()
    }

    fun updateProvince(value: String) {
        _province.value = value
        _city.value = ""
        updateDefaultPrices()
    }

    fun updateCity(value: String) {
        _city.value = value
        updateDefaultPrices()
    }

    fun updateIndustryType(value: String) {
        _industryType.value = value
    }

    fun updateElectricityType(value: String) {
        _electricityType.value = value
        updateDefaultPrices()
    }

    fun updatePeakPrice(value: String) {
        _peakPrice.value = value
    }

    fun updateValleyPrice(value: String) {
        _valleyPrice.value = value
    }

    fun updateNormalPrice(value: String) {
        _normalPrice.value = value
    }

    fun updateGenerationMode(value: String) {
        _generationMode.value = value
    }

    fun updateModulePower(value: Int) {
        _modulePower.value = value
    }

    fun updateInclination(value: String) {
        _inclination.value = value
    }

    fun updateSystemEfficiency(value: String) {
        _systemEfficiency.value = value
    }

    fun toggleProfessionalMode() {
        _isProfessionalMode.value = !_isProfessionalMode.value
    }

    private fun updateDefaultPrices() {
        if (_province.value.isNotEmpty() && _city.value.isNotEmpty()) {
            val priceType = when (_electricityType.value) {
                "residential" -> "residential"
                "commercial" -> "commercial"
                else -> "industrial"
            }
            val price = MockData.getElectricityPrice(_province.value, _city.value, priceType)
            if (price != null) {
                _peakPrice.value = price.peakPrice.toString()
                _valleyPrice.value = price.valleyPrice.toString()
                _normalPrice.value = price.normalPrice.toString()
            }
        }
    }

    // 执行测算
    fun calculate() {
        viewModelScope.launch {
            _isCalculating.value = true
            
            try {
                val params = CalculationParams(
                    roofArea = _roofArea.value.toDoubleOrNull() ?: 0.0,
                    scenario = _scenario.value,
                    province = _province.value,
                    city = _city.value,
                    industryType = _industryType.value,
                    electricityType = _electricityType.value,
                    peakPrice = _peakPrice.value.toDoubleOrNull() ?: 0.0,
                    valleyPrice = _valleyPrice.value.toDoubleOrNull() ?: 0.0,
                    normalPrice = _normalPrice.value.toDoubleOrNull() ?: 0.0,
                    generationMode = _generationMode.value,
                    modulePower = _modulePower.value,
                    inclination = _inclination.value.toDoubleOrNull() ?: 30.0,
                    systemEfficiency = (_systemEfficiency.value.toDoubleOrNull() ?: 82.0) / 100.0
                )

                val result = pvCalculator.calculate(params)
                _result.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isCalculating.value = false
            }
        }
    }

    // 保存记录
    fun saveRecord(clientId: Long? = null) {
        viewModelScope.launch {
            val result = _result.value ?: return@launch
            
            val record = CalculationRecord(
                clientId = clientId,
                roofArea = _roofArea.value.toDoubleOrNull() ?: 0.0,
                scenario = _scenario.value,
                province = _province.value,
                city = _city.value,
                industryType = if (_isProfessionalMode.value) _industryType.value else null,
                electricityType = if (_isProfessionalMode.value) _electricityType.value else null,
                peakPrice = result.totalInvestment,
                valleyPrice = result.monthlyProfit,
                normalPrice = result.yearlyProfit,
                generationMode = _generationMode.value,
                modulePower = _modulePower.value,
                inclination = _inclination.value.toDoubleOrNull() ?: 30.0,
                systemEfficiency = (_systemEfficiency.value.toDoubleOrNull() ?: 82.0) / 100.0,
                installedCapacity = result.installedCapacity,
                moduleCount = result.moduleCount,
                dailyGeneration = result.dailyGeneration,
                monthlyGeneration = result.monthlyGeneration,
                yearlyGeneration = result.yearlyGeneration,
                total25YearGeneration = result.total25YearGeneration,
                totalInvestment = result.totalInvestment,
                monthlyProfit = result.monthlyProfit,
                yearlyProfit = result.yearlyProfit,
                total25YearProfit = result.total25YearProfit,
                staticPayback = result.staticPayback,
                dynamicPayback = result.dynamicPayback,
                coverageRatio = result.coverageRatio,
                savingRate = result.savingRate,
                carbonReduction = result.carbonReduction,
                treeEquivalent = result.treeEquivalent
            )

            val id = repository.insertRecord(record)
            _savedRecordId.value = id
        }
    }
}