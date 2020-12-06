package com.ordolabs.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val gameId: Int,
    val date: Long // TODO: add converter (mapper)
)