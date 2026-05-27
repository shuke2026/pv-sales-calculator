package com.example.pvsalescalculator.data.mock

import com.example.pvsalescalculator.data.model.ElectricityPrice
import com.example.pvsalescalculator.data.model.IndustryElectricity
import com.example.pvsalescalculator.data.model.LocationData

object MockData {
    val locationList: List<LocationData> = listOf(
        LocationData("北京", "北京", 1450.0, 39.9042, 116.4074),
        LocationData("北京", "朝阳", 1420.0, 39.9242, 116.4792),
        LocationData("天津", "天津", 1400.0, 39.0842, 117.2008),
        LocationData("河北", "石家庄", 1500.0, 38.0423, 114.4792),
        LocationData("河北", "唐山", 1480.0, 39.6378, 118.1847),
        LocationData("山西", "太原", 1600.0, 37.8716, 112.5492),
        LocationData("内蒙古", "呼和浩特", 2600.0, 40.8175, 111.6706),
        LocationData("内蒙古", "包头", 2800.0, 40.6563, 109.8242),
        LocationData("辽宁", "沈阳", 1400.0, 41.8047, 123.4329),
        LocationData("辽宁", "大连", 1500.0, 38.9140, 121.6147),
        LocationData("吉林", "长春", 1350.0, 43.8868, 125.3231),
        LocationData("黑龙江", "哈尔滨", 1300.0, 45.8037, 126.5349),
        LocationData("上海", "上海", 1150.0, 31.2304, 121.4737),
        LocationData("江苏", "南京", 1200.0, 32.0603, 118.7969),
        LocationData("江苏", "苏州", 1180.0, 31.3260, 120.6201),
        LocationData("浙江", "杭州", 1250.0, 30.2741, 120.1552),
        LocationData("浙江", "宁波", 1300.0, 29.8739, 121.5408),
        LocationData("安徽", "合肥", 1200.0, 31.8652, 117.2272),
        LocationData("福建", "福州", 1400.0, 26.0789, 119.2965),
        LocationData("福建", "厦门", 1500.0, 24.4798, 118.0894),
        LocationData("江西", "南昌", 1300.0, 28.6895, 115.8921),
        LocationData("山东", "济南", 1400.0, 36.6760, 116.9886),
        LocationData("山东", "青岛", 1500.0, 36.0671, 120.3826),
        LocationData("河南", "郑州", 1450.0, 34.7579, 113.6243),
        LocationData("湖北", "武汉", 1200.0, 30.5928, 114.3055),
        LocationData("湖南", "长沙", 1300.0, 28.2280, 112.9388),
        LocationData("广东", "广州", 1500.0, 23.1291, 113.2644),
        LocationData("广东", "深圳", 1600.0, 22.5431, 114.0579),
        LocationData("广西", "南宁", 1450.0, 22.8175, 108.3200),
        LocationData("海南", "海口", 1800.0, 20.0339, 110.3597),
        LocationData("重庆", "重庆", 1100.0, 29.4316, 106.9123),
        LocationData("四川", "成都", 1050.0, 30.5728, 104.0668),
        LocationData("贵州", "贵阳", 1100.0, 26.5789, 106.7132),
        LocationData("云南", "昆明", 1800.0, 24.8820, 102.8329),
        LocationData("西藏", "拉萨", 3000.0, 29.6549, 91.1251),
        LocationData("陕西", "西安", 1400.0, 34.2619, 108.9463),
        LocationData("甘肃", "兰州", 2000.0, 36.0611, 103.8343),
        LocationData("青海", "西宁", 2600.0, 36.6171, 101.7798),
        LocationData("宁夏", "银川", 2800.0, 38.4741, 106.2729),
        LocationData("新疆", "乌鲁木齐", 2800.0, 43.8260, 87.6169),
        LocationData("新疆", "喀什", 2600.0, 39.4758, 75.9773)
    )

    val electricityPriceList: List<ElectricityPrice> = listOf(
        ElectricityPrice("北京", "北京", "residential", 0.85, 0.35, 0.52),
        ElectricityPrice("北京", "北京", "commercial", 1.20, 0.45, 0.85),
        ElectricityPrice("北京", "北京", "industrial", 0.75, 0.30, 0.55),
        ElectricityPrice("上海", "上海", "residential", 0.80, 0.30, 0.48),
        ElectricityPrice("上海", "上海", "commercial", 1.15, 0.42, 0.82),
        ElectricityPrice("上海", "上海", "industrial", 0.72, 0.28, 0.52),
        ElectricityPrice("广东", "广州", "residential", 0.68, 0.35, 0.51),
        ElectricityPrice("广东", "广州", "commercial", 1.05, 0.45, 0.78),
        ElectricityPrice("广东", "广州", "industrial", 0.68, 0.32, 0.50),
        ElectricityPrice("广东", "深圳", "residential", 0.70, 0.38, 0.53),
        ElectricityPrice("广东", "深圳", "commercial", 1.10, 0.48, 0.80),
        ElectricityPrice("广东", "深圳", "industrial", 0.70, 0.35, 0.52),
        ElectricityPrice("浙江", "杭州", "residential", 0.56, 0.30, 0.48),
        ElectricityPrice("浙江", "杭州", "commercial", 1.00, 0.40, 0.75),
        ElectricityPrice("浙江", "杭州", "industrial", 0.65, 0.28, 0.48),
        ElectricityPrice("江苏", "南京", "residential", 0.55, 0.28, 0.45),
        ElectricityPrice("江苏", "南京", "commercial", 0.98, 0.38, 0.72),
        ElectricityPrice("江苏", "南京", "industrial", 0.62, 0.26, 0.46),
        ElectricityPrice("山东", "济南", "residential", 0.54, 0.28, 0.44),
        ElectricityPrice("山东", "济南", "commercial", 0.95, 0.36, 0.70),
        ElectricityPrice("山东", "济南", "industrial", 0.60, 0.25, 0.44),
        ElectricityPrice("四川", "成都", "residential", 0.52, 0.25, 0.42),
        ElectricityPrice("四川", "成都", "commercial", 0.92, 0.35, 0.68),
        ElectricityPrice("四川", "成都", "industrial", 0.58, 0.24, 0.42),
        ElectricityPrice("内蒙古", "呼和浩特", "residential", 0.45, 0.20, 0.38),
        ElectricityPrice("内蒙古", "呼和浩特", "commercial", 0.80, 0.30, 0.60),
        ElectricityPrice("内蒙古", "呼和浩特", "industrial", 0.48, 0.18, 0.35),
        ElectricityPrice("新疆", "乌鲁木齐", "residential", 0.42, 0.18, 0.35),
        ElectricityPrice("新疆", "乌鲁木齐", "commercial", 0.78, 0.28, 0.58),
        ElectricityPrice("新疆", "乌鲁木齐", "industrial", 0.45, 0.16, 0.32),
        ElectricityPrice("西藏", "拉萨", "residential", 0.50, 0.22, 0.40),
        ElectricityPrice("西藏", "拉萨", "commercial", 0.85, 0.32, 0.65),
        ElectricityPrice("西藏", "拉萨", "industrial", 0.52, 0.20, 0.38)
    )

    val industryElectricityList: List<IndustryElectricity> = listOf(
        IndustryElectricity("manufacturing", 120.0, "制造业"),
        IndustryElectricity("chemical", 200.0, "化工"),
        IndustryElectricity("food", 80.0, "食品加工"),
        IndustryElectricity("metal", 250.0, "金属加工"),
        IndustryElectricity("textile", 150.0, "纺织"),
        IndustryElectricity("pharmaceutical", 100.0, "制药"),
        IndustryElectricity("logistics", 30.0, "物流仓储"),
        IndustryElectricity("retail", 50.0, "商业零售")
    )

    fun getProvinces(): List<String> {
        return locationList.map { it.province }.distinct().sorted()
    }

    fun getCitiesByProvince(province: String): List<String> {
        return locationList.filter { it.province == province }.map { it.city }.distinct()
    }

    fun getLocationData(province: String, city: String): LocationData? {
        return locationList.find { it.province == province && it.city == city }
    }

    fun getElectricityPrice(province: String, city: String, priceType: String): ElectricityPrice? {
        return electricityPriceList.find {
            it.province == province && it.city == city && it.priceType == priceType
        }
    }

    fun getIndustryElectricity(industryType: String): IndustryElectricity? {
        return industryElectricityList.find { it.industryType == industryType }
    }

    fun getDefaultElectricityPrice(scenario: String): ElectricityPrice {
        return when (scenario) {
            "residential" -> electricityPriceList.first { it.priceType == "residential" }
            "commercial" -> electricityPriceList.first { it.priceType == "commercial" }
            else -> electricityPriceList.first { it.priceType == "industrial" }
        }
    }
}

object PriceConstants {
    const val MODULE_COST_PER_WATT = 1.8 // 元/W
    const val INVERTER_COST_PER_WATT = 0.5 // 元/W
    const val INSTALLATION_COST_PER_WATT = 0.7 // 元/W
    const val CARBON_EMISSION_FACTOR = 0.58 // kg CO₂/kWh
    const val TREE_ABSORPTION_PER_YEAR = 20 // kg CO₂/棵/年
    const val DISCOUNT_RATE = 0.04 // 折现率
    const val SYSTEM_EFFICIENCY_DEFAULT = 0.82
    const val INCLINATION_DEFAULT = 30.0
}

object IndustryConstants {
    const val RESIDENTIAL_KWH_PER_SQM = 15.0 // 户用每平米月用电量
    const val COMMERCIAL_KWH_PER_SQM = 40.0 // 工商业每平米月用电量
    const val FACTORY_KWH_PER_SQM = 120.0 // 工厂每平米月用电量
}