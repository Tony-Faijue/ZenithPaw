package com.example.zenithpaw.ui.navigation

/**
 * The basic screen routes for the app
 */
public sealed class Screen (val route: String) {
    object Main: Screen("home")
    object Shop: Screen("shop")
    object Profile: Screen("profile")
    object Task: Screen("tasks")
    object TaskDetails: Screen("task_details/{taskId}"){
        fun passId(id: String) = "tasks/$id"
    }
    object Pets: Screen("pets")
    object PetDetails: Screen("pet_details/{petId}"){
        fun passId(id: String) = "pets/$id"
    }
    object Registration: Screen("registration")
    object Login: Screen("login")
}