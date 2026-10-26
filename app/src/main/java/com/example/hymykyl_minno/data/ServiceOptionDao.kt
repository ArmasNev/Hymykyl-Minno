package com.example.hymykyl_minno.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceOptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceOption)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceOption>)

    @Update
    suspend fun updateService(service: ServiceOption)

    @Delete
    suspend fun deleteService(service: ServiceOption)

    @Query("SELECT * FROM service_options ORDER BY displayOrder ASC")
    fun getAllServices(): Flow<List<ServiceOption>>

    @Query("SELECT COUNT(*) FROM service_options")
    suspend fun getServiceCount(): Int
}
