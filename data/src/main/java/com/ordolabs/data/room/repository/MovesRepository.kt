package com.ordolabs.data.room.repository

import com.ordolabs.data.room.dao.MovesDao
import com.ordolabs.domain.repository.IRoomMovesRepository

class MovesRepository(private val dao: MovesDao) : IRoomMovesRepository {
}