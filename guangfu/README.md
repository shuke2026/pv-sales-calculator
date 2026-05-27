# PV Sales Calculator - 光伏销售端智能测算系统

一款面向光伏销售人员的专业智能测算 App，支持快速谈单、现场测算、一键生成专业报告。

## 功能特性

### 双测算模式
- **简易测算**：输入屋顶面积、安装场景、所在省市，3步出结果
- **专业测算**：支持发电模式切换、电价微调、组件选型等高级参数

### 核心测算指标
- 装机容量、组件数量
- 日均/月均/年发电量
- 项目总投资
- 月/年节电收益
- 25年总收益
- 静态/动态回本周期
- 用电覆盖比例、节电率
- 碳减排量、等效植树量

### 客户档案管理
- 客户信息增删改查
- 支持搜索功能
- 历史测算记录关联

### 报告生成
- 一键生成专业 PDF 报告
- 支持分享和保存

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose
- **架构**: MVVM + ViewModel + StateFlow
- **数据库**: Room (本地数据库)
- **图表**: MPAndroidChart (预留)
- **PDF**: Android PDF API

## 内置数据

- 全国40+省市日照小时数据库
- 各行业工厂标准月用电量
- 各地工商业/居民峰谷平电价
- 组件、逆变器、安装辅材价格基准

## 支持场景

- 户用光伏
- 工商业光伏
- 高耗电工厂

## 离线可用

所有核心数据本地内置，无需联网即可测算。

## 构建说明

```bash
# 克隆仓库
git clone https://github.com/yourusername/pv-sales-calculator.git
cd pv-sales-calculator

# 编译构建
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

## 发布说明

项目已配置 GitHub Actions，每次推送到 main 分支会自动构建 APK 并发布到 Releases。

## License

MIT License