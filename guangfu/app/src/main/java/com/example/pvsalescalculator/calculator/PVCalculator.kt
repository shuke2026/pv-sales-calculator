package com.example.pvsalescalculator.calculator

import com.example.pvsalescalculator.data.mock.MockData
import com.example.pvsalescalculator.data.mock.PriceConstants
import com.example.pvsalescalculator.data.mock.IndustryConstants

data class CalculationParams(
    val roofArea: Double,
    val scenario: String,
    val province: String,
    val city: String,
    val industryType: String = "manufacturing",
    val electricityType: String = "commercial",
    val peakPrice: Double = 0.0,
    val valleyPrice: Double = 0.0,
    val normalPrice: Double = 0.0,
    val generationMode: String = "self_use",
    val modulePower: Int = 500,
    val inclination: Double = PriceConstants.INCLINATION_DEFAULT,
    val systemEfficiency: Double = PriceConstants.SYSTEM_EFFICIENCY_DEFAULT
)

data class CalculationResult(
    val installedCapacity: Double,
    val moduleCount: Int,
    val dailyGeneration: Double,
    val monthlyGeneration: Double,
    val yearlyGeneration: Double,
    val total25YearGeneration: Double,
    val totalInvestment: Double,
    val monthlyProfit: Double,
    val yearlyProfit: Double,
    val total25YearProfit: Double,
    val staticPayback: Double,
    val dynamicPayback: Double,
    val coverageRatio: Double,
    val savingRate: Double,
    val carbonReduction: Double,
    val treeEquivalent: Int
)

class PVCalculator {
    fun calculate(params: CalculationParams): CalculationResult {
        // 获取日照小时数
        val locationData = MockData.getLocationData(params.province, params.city)
        val annualSunshineHours = locationData?.annualSunshineHours ?: 1500.0

        // 获取电价数据
        var peakPrice = params.peakPrice
        var valleyPrice = params.valleyPrice
        var normalPrice = params.normalPrice

        if (peakPrice == 0.0 || valleyPrice == 0.0 || normalPrice == 0.0) {
            val priceType = when (params.electricityType) {
                "residential" -> "residential"
                "commercial" -> "commercial"
                else -> "industrial"
            }
            val electricityPrice = MockData.getElectricityPrice(params.province, params.city, priceType)
                ?: MockData.getDefaultElectricityPrice(priceType)
            if (peakPrice == 0.0) peakPrice = electricityPrice.peakPrice
            if (valleyPrice == 0.0) valleyPrice = electricityPrice.valleyPrice
            if (normalPrice == 0.0) normalPrice = electricityPrice.normalPrice
        }

        // 计算装机容量 (假设每平米安装180W)
        val powerPerSqm = 180.0 // W/㎡
        val installedCapacityW = params.roofArea * powerPerSqm
        val installedCapacity = installedCapacityW / 1000 // kW

        // 计算组件数量
        val moduleCount = Math.ceil(installedCapacityW / params.modulePower).toInt()

        // 发电量计算
        // 年发电量 = 装机容量(kW) × 年日照小时数 × 系统效率
        val yearlyGeneration = installedCapacity * annualSunshineHours * params.systemEfficiency
        val dailyGeneration = yearlyGeneration / 365
        val monthlyGeneration = yearlyGeneration / 12
        val total25YearGeneration = yearlyGeneration * 25

        // 计算月用电量
        val monthlyElectricity = getMonthlyElectricity(params.scenario, params.roofArea, params.industryType)

        // 收益计算
        val (monthlyProfit, yearlyProfit) = calculateProfit(
            generationMode = params.generationMode,
            monthlyGeneration = monthlyGeneration,
            monthlyElectricity = monthlyElectricity,
            peakPrice = peakPrice,
            valleyPrice = valleyPrice,
            normalPrice = normalPrice
        )
        val total25YearProfit = calculateTotal25YearProfit(yearlyProfit)

        // 投资计算
        val totalInvestment = calculateInvestment(installedCapacityW)

        // 回本周期计算
        val staticPayback = calculateStaticPayback(totalInvestment, yearlyProfit)
        val dynamicPayback = calculateDynamicPayback(totalInvestment, yearlyProfit)

        // 覆盖比例和节电率
        val coverageRatio = Math.min(monthlyGeneration / monthlyElectricity * 100, 100.0)
        val savingRate = coverageRatio

        // 碳减排计算
        val carbonReduction = yearlyGeneration * PriceConstants.CARBON_EMISSION_FACTOR / 1000 * 25
        val treeEquivalent = Math.round(carbonReduction * 1000 / PriceConstants.TREE_ABSORPTION_PER_YEAR / 25).toInt()

        return CalculationResult(
            installedCapacity = Math.round(installedCapacity * 100) / 100.0,
            moduleCount = moduleCount,
            dailyGeneration = Math.round(dailyGeneration * 100) / 100.0,
            monthlyGeneration = Math.round(monthlyGeneration * 100) / 100.0,
            yearlyGeneration = Math.round(yearlyGeneration * 100) / 100.0,
            total25YearGeneration = Math.round(total25YearGeneration * 100) / 100.0,
            totalInvestment = Math.round(totalInvestment * 100) / 100.0,
            monthlyProfit = Math.round(monthlyProfit * 100) / 100.0,
            yearlyProfit = Math.round(yearlyProfit * 100) / 100.0,
            total25YearProfit = Math.round(total25YearProfit * 100) / 100.0,
            staticPayback = Math.round(staticPayback * 10) / 10.0,
            dynamicPayback = Math.round(dynamicPayback * 10) / 10.0,
            coverageRatio = Math.round(coverageRatio * 10) / 10.0,
            savingRate = Math.round(savingRate * 10) / 10.0,
            carbonReduction = Math.round(carbonReduction * 100) / 100.0,
            treeEquivalent = treeEquivalent
        )
    }

    private fun getMonthlyElectricity(scenario: String, roofArea: Double, industryType: String): Double {
        return when (scenario) {
            "residential" -> roofArea * IndustryConstants.RESIDENTIAL_KWH_PER_SQM
            "commercial" -> roofArea * IndustryConstants.COMMERCIAL_KWH_PER_SQM
            "factory" -> {
                val industry = MockData.getIndustryElectricity(industryType)
                roofArea * (industry?.monthlyKwhPerSqm ?: IndustryConstants.FACTORY_KWH_PER_SQM)
            }
            else -> roofArea * IndustryConstants.COMMERCIAL_KWH_PER_SQM
        }
    }

    private fun calculateProfit(
        generationMode: String,
        monthlyGeneration: Double,
        monthlyElectricity: Double,
        peakPrice: Double,
        valleyPrice: Double,
        normalPrice: Double
    ): Pair<Double, Double> {
        val peakRatio = 0.3 // 峰值用电比例
        val valleyRatio = 0.2 // 谷值用电比例
        val normalRatio = 0.5 // 平值用电比例

        return when (generationMode) {
            "full_feed" -> {
                // 全额上网：全部按脱硫煤电价结算，约0.39元/kWh
                val feedPrice = 0.39
                val monthly = monthlyGeneration * feedPrice
                Pair(monthly, monthly * 12)
            }
            "self_use" -> {
                // 自发自用：按峰谷平电价计算节省
                val peakSave = monthlyGeneration * peakRatio * peakPrice
                val valleySave = monthlyGeneration * valleyRatio * valleyPrice
                val normalSave = monthlyGeneration * normalRatio * normalPrice
                val monthly = peakSave + valleySave + normalSave
                Pair(Math.min(monthly, monthlyElectricity * normalPrice), monthly * 12)
            }
            "excess_feed" -> {
                // 余电上网：自发自用部分按电价节省，余电按上网电价
                val selfUse = Math.min(monthlyGeneration, monthlyElectricity)
                val excess = monthlyGeneration - selfUse
                
                val peakSave = selfUse * peakRatio * peakPrice
                val valleySave = selfUse * valleyRatio * valleyPrice
                val normalSave = selfUse * normalRatio * normalPrice
                val feedIncome = excess * 0.39
                
                val monthly = peakSave + valleySave + normalSave + feedIncome
                Pair(monthly, monthly * 12)
            }
            else -> {
                val monthly = monthlyGeneration * normalPrice
                Pair(monthly, monthly * 12)
            }
        }
    }

    private fun calculateTotal25YearProfit(yearlyProfit: Double): Double {
        var total = 0.0
        for (year in 1..25) {
            total += yearlyProfit / Math.pow(1 + PriceConstants.DISCOUNT_RATE, year.toDouble())
        }
        return total
    }

    private fun calculateInvestment(installedCapacityW: Double): Double {
        val moduleCost = installedCapacityW * PriceConstants.MODULE_COST_PER_WATT
        val inverterCost = installedCapacityW * PriceConstants.INVERTER_COST_PER_WATT
        val installationCost = installedCapacityW * PriceConstants.INSTALLATION_COST_PER_WATT
        return moduleCost + inverterCost + installationCost
    }

    private fun calculateStaticPayback(totalInvestment: Double, yearlyProfit: Double): Double {
        return if (yearlyProfit > 0) totalInvestment / yearlyProfit else Double.MAX_VALUE
    }

    private fun calculateDynamicPayback(totalInvestment: Double, yearlyProfit: Double): Double {
        if (yearlyProfit <= 0) return Double.MAX_VALUE

        var cumulativePresentValue = 0.0
        var paybackYears = 0.0

        for (year in 1..25) {
            cumulativePresentValue += yearlyProfit / Math.pow(1 + PriceConstants.DISCOUNT_RATE, year.toDouble())
            if (cumulativePresentValue >= totalInvestment) {
                paybackYears = year.toDouble()
                break
            }
        }

        return if (paybackYears > 0) paybackYears else 25.0
    }

    fun generateYearlyData(result: CalculationResult): Pair<DoubleArray, DoubleArray> {
        val years = DoubleArray(25)
        val cumulativeProfit = DoubleArray(25)
        val yearlyProfit = result.yearlyProfit
        val totalInvestment = result.totalInvestment

        var cumulative = -totalInvestment
        for (i in 0..24) {
            years[i] = (i + 1).toDouble()
            cumulative += yearlyProfit / Math.pow(1 + PriceConstants.DISCOUNT_RATE, (i + 1).toDouble())
            cumulativeProfit[i] = Math.round(cumulative * 100) / 100.0
        }

        return Pair(years, cumulativeProfit)
    }
}