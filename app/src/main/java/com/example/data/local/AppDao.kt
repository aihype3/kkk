package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LotDao {
    @Query("SELECT * FROM lots ORDER BY timestamp DESC")
    fun getAllLots(): Flow<List<LotEntity>>

    @Query("SELECT * FROM lots WHERE lotId = :lotId LIMIT 1")
    suspend fun getLotById(lotId: String): LotEntity?

    @Query("SELECT * FROM lots WHERE paymentStatus = :status ORDER BY timestamp DESC")
    fun getLotsByPaymentStatus(status: String): Flow<List<LotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLot(lot: LotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lots: List<LotEntity>)

    @Update
    suspend fun updateLot(lot: LotEntity)

    @Query("UPDATE lots SET status = :status, paymentStatus = :paymentStatus WHERE lotId = :lotId")
    suspend fun updateLotStatus(lotId: String, status: String, paymentStatus: String)

    @Query("UPDATE lots SET isSynced = 1 WHERE isSynced = 0")
    suspend fun syncAllLots()
}

@Dao
interface RecyclerDao {
    @Query("SELECT * FROM recyclers ORDER BY isAuthorized DESC, rating DESC, distanceKm ASC")
    fun getAllRecyclers(): Flow<List<RecyclerEntity>>

    @Query("SELECT * FROM recyclers WHERE id = :id LIMIT 1")
    suspend fun getRecyclerById(id: String): RecyclerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recyclers: List<RecyclerEntity>)
}

@Dao
interface PriceRateDao {
    @Query("SELECT * FROM price_rates ORDER BY pricePerKg DESC")
    fun getAllRates(): Flow<List<PriceRateEntity>>

    @Query("SELECT * FROM price_rates WHERE materialId = :materialId LIMIT 1")
    suspend fun getRateByMaterial(materialId: String): PriceRateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<PriceRateEntity>)
}

@Dao
interface SafetyTipDao {
    @Query("SELECT * FROM safety_tips")
    fun getAllTips(): Flow<List<SafetyTipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tips: List<SafetyTipEntity>)
}
