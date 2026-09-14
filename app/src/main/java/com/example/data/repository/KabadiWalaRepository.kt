package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.LotEntity
import com.example.data.local.PriceRateEntity
import com.example.data.local.RecyclerEntity
import com.example.data.local.SafetyTipEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class DashboardStats(
    val todayEarnings: Int,
    val pendingPayment: Int,
    val lotsSold: Int,
    val totalEarnings: Int,
    val thisMonthEarnings: Int
)

class KabadiWalaRepository(private val database: AppDatabase) {
    val allLots: Flow<List<LotEntity>> = database.lotDao().getAllLots()
    val allRecyclers: Flow<List<RecyclerEntity>> = database.recyclerDao().getAllRecyclers()
    val allRates: Flow<List<PriceRateEntity>> = database.priceRateDao().getAllRates()
    val allSafetyTips: Flow<List<SafetyTipEntity>> = database.safetyTipDao().getAllTips()

    val stats: Flow<DashboardStats> = allLots.map { lots ->
        val now = System.currentTimeMillis()
        val oneDayAgo = now - 86400000L
        val thirtyDaysAgo = now - 86400000L * 30L

        var today = 0
        var pending = 0
        var total = 0
        var thisMonth = 0
        var soldCount = 0

        for (lot in lots) {
            val amount = lot.finalPrice.takeIf { it > 0 } ?: ((lot.estimatedMinPrice + lot.estimatedMaxPrice) / 2)
            if (lot.paymentStatus.equals("Paid", ignoreCase = true)) {
                total += amount
                if (lot.timestamp >= oneDayAgo) {
                    today += amount
                }
                if (lot.timestamp >= thirtyDaysAgo) {
                    thisMonth += amount
                }
                soldCount++
            } else {
                pending += amount
            }
        }

        DashboardStats(
            todayEarnings = today,
            pendingPayment = pending,
            lotsSold = soldCount,
            totalEarnings = total,
            thisMonthEarnings = thisMonth
        )
    }

    suspend fun insertLot(lot: LotEntity) {
        database.lotDao().insertLot(lot)
    }

    suspend fun updateLot(lot: LotEntity) {
        database.lotDao().updateLot(lot)
    }

    suspend fun confirmHandover(
        lotId: String,
        actualWeightKg: Double,
        finalPrice: Int,
        paymentMethod: String,
        paymentStatus: String
    ) {
        val existing = database.lotDao().getLotById(lotId)
        if (existing != null) {
            val updated = existing.copy(
                approximateWeightKg = actualWeightKg,
                finalPrice = finalPrice,
                paymentMethod = paymentMethod,
                paymentStatus = paymentStatus,
                status = if (paymentStatus.equals("Paid", ignoreCase = true)) "PAID" else "CONFIRMED",
                handoverReference = "HR-${(10000..99999).random()}"
            )
            database.lotDao().updateLot(updated)
        }
    }

    suspend fun syncAllOfflineData() {
        database.lotDao().syncAllLots()
    }
}
