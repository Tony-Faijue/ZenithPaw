package com.example.zenithpaw.ui.uievents

/**
 * The basic navigation events for the app
 */
sealed interface NavigationEvent {
    /**
     * Navigates to a route
     */
    data class Navigate(
        /**
         * The route to navigate to
         */
        val route: String,
        /**
         * The route the back stack clears up to
         */
        val popUpToRoute: String? = null,
        /**
         * Whether the current route is included in the back stack clearing
         */
        val inclusive: Boolean = false
        ): NavigationEvent

    /**
     * Navigates back to a previous route
     */
    object NavigateBack: NavigationEvent
}