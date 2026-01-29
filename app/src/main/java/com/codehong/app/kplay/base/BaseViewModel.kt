package com.codehong.app.kplay.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> : ViewModel() {

    // 1. 초기 상태 설정 단순화
    protected abstract fun createInitialState(): UiState

    // 2. State: Atomic한 업데이트를 위해 MutableStateFlow 사용
    // lazy를 제거하고 바로 초기화하여 깔끔하게 처리
    private val _state: MutableStateFlow<UiState> by lazy { MutableStateFlow(createInitialState()) }
    val state: StateFlow<UiState> by lazy { _state.asStateFlow() }

    // 3. Event: 버퍼를 두어 UI 스레드에서 지연 없이 이벤트 수신 (tryEmit 사용 가능)
    // extraBufferCapacity를 설정하면 emit 시 suspend 되지 않아 UI가 버벅이지 않음
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow(extraBufferCapacity = 64)
    val event: SharedFlow<Event> = _event.asSharedFlow()

    // 4. Effect: 한 번만 소비되는 이벤트 (Toast, Navigation 등)
    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    /**
     * 이벤트를 구독하고 처리합니다.
     * 복잡한 변환(Debounce 등)이 필요하면 이 메서드를 오버라이드 하거나
     * handleEvents 내부에서 별도 Flow를 구축할 수도 있습니다.
     */
    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect { event ->
                handleEvents(event)
            }
        }
    }

    abstract fun handleEvents(event: Event)

    /**
     * UI에서 이벤트를 발생시키는 진입점.
     * launch를 매번 생성하는 오버헤드를 줄이기 위해 tryEmit 사용.
     */
    fun setEvent(event: Event) {
        // tryEmit은 버퍼가 여유 있으면 즉시 성공하고 true 반환 (Non-blocking)
        // 버퍼가 꽉 차면 false를 반환하지만, 64개 버퍼면 UI 이벤트로는 충분함
        _event.tryEmit(event)
    }

    /**
     * 상태 업데이트 (Thread-Safe)
     * update {} 블록은 CAS(Compare-And-Swap) 알고리즘을 사용하여
     * 동시성 문제 없이 안전하게 상태를 갱신합니다.
     */
    protected fun setState(reducer: UiState.() -> UiState) {
        _state.update { oldState ->
            oldState.reducer()
        }
    }

    /**
     * 부수 효과 발생 (Fire-and-forget)
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }
}