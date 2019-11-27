package com.polairapp.polair

sealed class ScreenStates {
    object MainMap: ScreenStates()
    object LetsGo: ScreenStates()
    object PathTraced: ScreenStates()
}
