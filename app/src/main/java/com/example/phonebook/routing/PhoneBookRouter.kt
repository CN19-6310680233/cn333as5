package com.example.phonebook.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


sealed class Screen {
    object List: Screen()
    object Update: Screen()
    object Delete: Screen()
}

object PhoneBookRouter {
    var currentScreen: Screen by mutableStateOf(Screen.List)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}