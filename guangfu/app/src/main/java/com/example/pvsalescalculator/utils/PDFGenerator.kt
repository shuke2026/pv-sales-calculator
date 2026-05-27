package com.example.pvsalescalculator.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.pvsalescalculator.data.database.entity.CalculationRecord
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PDFGenerator(private val context: Context) {

    private val df = DecimalFormat("#,##0.00")
    private val dfInt = DecimalFormat("#,##0")

    fun generateReport(record: CalculationRecord, companyName: String = "光伏科技有限公司"): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        paint.color = Color.BLACK
        paint.textSize = 18f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(companyName, 297.5f, 50f, paint)

        paint.textSize = 24f
        paint.isBold = true
        canvas.drawText("光伏项目测算报告", 297.5f, 80f, paint)

        paint.textSize = 12f
        paint.isBold = false
        paint.textAlign = Paint.Align.LEFT

        // 项目概况
        var y = 120f
        canvas.drawText("【项目概况】", 50f, y, paint)
        y += 25f

        paint.textSize = 11f
        canvas.drawText("测算日期：${getCurrentDate()}", 50f, y, paint)
        y += 18f
        canvas.drawText("安装场景：${getScenarioText(record.scenario)}", 50f, y, paint)
        y += 18f
        canvas.drawText("所在地点：${record.province} ${record.city}", 50f, y, paint)
        y += 18f
        canvas.drawText("屋顶面积：${df.format(record.roofArea)} ㎡", 50f, y, paint)
        y += 18f
        canvas.drawText("发电模式：${getGenerationModeText(record.generationMode)}", 50f, y, paint)
        y += 18f
        canvas.drawText("组件功率：${record.modulePower}W", 50f, y, paint)
        y += 25f

        // 装机信息
        canvas.drawText("【装机信息】", 50f, y, paint)
        y += 25f
        canvas.drawText("装机容量：${df.format(record.installedCapacity)} kW", 50f, y, paint)
        y += 18f
        canvas.drawText("组件数量：${dfInt.format(record.moduleCount)} 块", 50f, y, paint)
        y += 18f
        canvas.drawText("安装倾角：${df.format(record.inclination)}°", 50f, y, paint)
        y += 18f
        canvas.drawText("系统效率：${df.format(record.systemEfficiency * 100)}%", 50f, y, paint)
        y += 25f

        // 发电量
        canvas.drawText("【发电量估算】", 50f, y, paint)
        y += 25f
        canvas.drawText("日均发电：${df.format(record.dailyGeneration)} kWh", 50f, y, paint)
        y += 18f
        canvas.drawText("月均发电：${df.format(record.monthlyGeneration)} kWh", 50f, y, paint)
        y += 18f
        canvas.drawText("年发电量：${df.format(record.yearlyGeneration)} kWh", 50f, y, paint)
        y += 18f
        canvas.drawText("25年总发电：${df.format(record.total25YearGeneration)} kWh", 50f, y, paint)
        y += 25f

        // 投资与收益
        canvas.drawText("【投资与收益】", 50f, y, paint)
        y += 25f
        canvas.drawText("项目总投资：¥ ${df.format(record.totalInvestment)}", 50f, y, paint)
        y += 18f
        canvas.drawText("月节电收益：¥ ${df.format(record.monthlyProfit)}", 50f, y, paint)
        y += 18f
        canvas.drawText("年节电收益：¥ ${df.format(record.yearlyProfit)}", 50f, y, paint)
        y += 18f
        canvas.drawText("25年总收益：¥ ${df.format(record.total25YearProfit)}", 50f, y, paint)
        y += 25f

        // 回本周期
        canvas.drawText("【回本周期】", 50f, y, paint)
        y += 25f
        canvas.drawText("静态回本周期：${df.format(record.staticPayback)} 年", 50f, y, paint)
        y += 18f
        canvas.drawText("动态回本周期：${df.format(record.dynamicPayback)} 年", 50f, y, paint)
        y += 25f

        // 环保效益
        canvas.drawText("【环保效益】", 50f, y, paint)
        y += 25f
        canvas.drawText("用电覆盖比例：${df.format(record.coverageRatio)}%", 50f, y, paint)
        y += 18f
        canvas.drawText("节电率：${df.format(record.savingRate)}%", 50f, y, paint)
        y += 18f
        canvas.drawText("25年碳减排量：${df.format(record.carbonReduction)} 吨CO₂", 50f, y, paint)
        y += 18f
        canvas.drawText("等效植树：${dfInt.format(record.treeEquivalent)} 棵", 50f, y, paint)

        document.finishPage(page)

        // 第二页 - 25年收益曲线（简化为文本表格）
        val pageInfo2 = PdfDocument.PageInfo.Builder(595, 842, 2).create()
        val page2 = document.startPage(pageInfo2)
        val canvas2 = page2.canvas

        paint.textSize = 18f
        paint.textAlign = Paint.Align.CENTER
        canvas2.drawText("25年收益预测表", 297.5f, 50f, paint)

        paint.textSize = 10f
        paint.textAlign = Paint.Align.CENTER

        // 表头
        y = 80f
        canvas2.drawText("年份", 80f, y, paint)
        canvas2.drawText("年收益(元)", 200f, y, paint)
        canvas2.drawText("累计收益(元)", 350f, y, paint)
        canvas2.drawText("投资状态", 480f, y, paint)
        y += 20f

        // 绘制数据
        var cumulativeProfit = -record.totalInvestment
        for (year in 1..25) {
            val yearlyProfit = record.yearlyProfit
            cumulativeProfit += yearlyProfit

            canvas2.drawText(year.toString(), 80f, y, paint)
            canvas2.drawText(df.format(yearlyProfit), 200f, y, paint)
            canvas2.drawText(df.format(cumulativeProfit), 350f, y, paint)
            canvas2.drawText(if (cumulativeProfit >= 0) "盈利" else "亏损", 480f, y, paint)
            y += 18f

            if (y > 800 && year < 25) {
                document.finishPage(page2)
                val newPage = PdfDocument.PageInfo.Builder(595, 842, document.pages.size + 1).create()
                val nextCanvas = document.startPage(newPage).canvas
                y = 50f
                paint.textSize = 10f
                canvas2 = nextCanvas
            }
        }

        document.finishPage(page2)

        // 保存文件
        val fileName = "光伏测算报告_${getCurrentDate()}.pdf"
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PVReports")
        directory.mkdirs()
        val file = File(directory, fileName)

        FileOutputStream(file).use {
            document.writeTo(it)
        }

        document.close()
        return file
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault()).format(Date())
    }

    private fun getScenarioText(scenario: String): String {
        return when (scenario) {
            "residential" -> "户用"
            "commercial" -> "工商业"
            "factory" -> "工厂"
            else -> scenario
        }
    }

    private fun getGenerationModeText(mode: String): String {
        return when (mode) {
            "full_feed" -> "全额上网"
            "self_use" -> "自发自用"
            "excess_feed" -> "余电上网"
            else -> mode
        }
    }
}