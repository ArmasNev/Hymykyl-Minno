package com.example.hymykyl_minno.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdminUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminUser)

    @Query("SELECT * FROM admin_users WHERE username = :username LIMIT 1")
    suspend fun getAdminByUsername(username: String): AdminUser?

    @Query("SELECT COUNT(*) FROM admin_users")
    suspend fun getAdminCount(): Int
}
