package com.example.zenithpaw.ui.viewmodels

import com.example.zenithpaw.ui.uievents.NavigationEvent
import kotlinx.coroutines.flow.Flow

/**
 * An interface for a ViewModel that can navigate to a route
 */
interface NavigableViewModel {
    /**
     * A flow of navigation event
     */
    val navigationEvent: Flow<NavigationEvent>
    /**
     * Navigates to a route
     * @param route the route to navigate to
     * @param popUpToRoute the route the back stack clears up to
     * @param inclusive whether the current route is included in the back stack clearing
     */
    fun navigateTo(route: String, popUpToRoute: String? = null, inclusive: Boolean = false)
    /**
     * Navigates back to a previous route
     */
    fun navigateBack()
}