package com.example.zenithpaw.ui.uiscreens

/**
 * The basic screen routes for the app
 */
public sealed class Screen (val route: String) {
    object Main: Screen("home")
    object Shop: Screen("shop")
    object Profile: Screen("profile")
    object Task: Screen("tasks")
    object Pets: Screen("pets")
    object Registration: Screen("registration")
}