package com.ordolabs.domain.usecase

interface BaseUsecase<in Parameter, out Result> {
    suspend operator fun invoke(params: Parameter): Result
}