package com.ordolabs.domain.usecase

/**
 * Basic use case.
 *
 * @param Parameter type of object, which would be passed to [invoke].
 * @param Result type of object, whick would be returned from [invoke].
 */
interface BaseUsecase<in Parameter, out Result> {
    suspend operator fun invoke(params: Parameter): Result
}