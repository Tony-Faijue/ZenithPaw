package com.example.zenithpaw.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenithpaw.ui.uievents.NavigationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * An abstract ViewModel class that implements the NavigableViewModel interface and ViewModel constructor
 */
abstract class BaseViewModel: ViewModel(), NavigableViewModel {
    // Cold stream for navigation events which are single one-off events
    private val _navigationEvent = Channel<NavigationEvent>()
    override val navigationEvent: Flow<NavigationEvent> = _navigationEvent.receiveAsFlow()

    override fun navigateTo(route: String, popUpToRoute: String?, inclusive: Boolean) {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.Navigate(route, popUpToRoute, inclusive))
        }
    }
    override fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateBack)
        }
    }
}