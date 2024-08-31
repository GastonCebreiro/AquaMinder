package com.example.aquaminder.feature_login.data.local.entity


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String?,
    var mail: String?,
    var password: String?

) : Parcelable

fun UserEntity.toDomainModel() = UserDomainModel(
    name = name.orEmpty(),
    mail = mail.orEmpty(),
    password = password.orEmpty()
)