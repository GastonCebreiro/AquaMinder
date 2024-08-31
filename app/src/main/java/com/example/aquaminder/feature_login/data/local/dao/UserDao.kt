package com.example.aquaminder.feature_login.data.local.dao
import androidx.room.*
import com.example.aquaminder.feature_login.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getUser(name: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}