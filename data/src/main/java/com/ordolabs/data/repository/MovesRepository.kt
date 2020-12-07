package com.ordolabs.data.repository

import com.ordolabs.data.dao.MovesDao
import com.ordolabs.domain.repository.IDBMovesRepository

class MovesRepository(private val dao: MovesDao) : IDBMovesRepository {
}