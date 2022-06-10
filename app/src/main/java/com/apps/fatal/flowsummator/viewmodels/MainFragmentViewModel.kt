package com.apps.fatal.flowsummator.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MainFragmentViewModel : ViewModel() {

    private companion object {

        const val DELAY = 100L
    }

    private var adderFlowContext: CoroutineScope? = null

    fun collectAdderFlow(n: Int, onAdderFlowEmit: OnAdderFlowEmit) {
        adderFlowContext?.cancel()
        adderFlowContext = CoroutineScope(Dispatchers.Default + CoroutineName("AdderFlow"))

        adderFlowContext?.launch {
            val flowsArr = Array(n) { i ->
                flow {
                    val value = i + 1
                    delay(value * DELAY)
                    emit(value)
                }
            }

            var sum = 0
            flowsArr.collectAll {
                sum += it
                withContext(Dispatchers.Main) {
                    onAdderFlowEmit.onEmit(sum)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun <T> Array<Flow<T>>.collectAll(onEmit: suspend (value: T) -> Unit) {
        channelFlow {
            forEach { flow ->
                launch { flow.collect { send(it) } }
            }
        }.collect { value ->
            onEmit.invoke(value)
        }
    }

    fun interface OnAdderFlowEmit {
        fun onEmit(i: Int)
    }
}