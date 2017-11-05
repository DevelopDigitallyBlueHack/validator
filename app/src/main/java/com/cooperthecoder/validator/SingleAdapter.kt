package com.cooperthecoder.validator

import io.fotoapparat.result.adapter.Adapter
import io.reactivex.Single
import java.util.concurrent.Future


class SingleAdapter<T>: Adapter<T, Single<T>> {

    companion object {
        fun <R> toSingle(): SingleAdapter<R> {
            return SingleAdapter()
        }
    }

    override fun adapt(future: Future<T>): Single<T> {
        return Single.fromFuture(future)
    }
}