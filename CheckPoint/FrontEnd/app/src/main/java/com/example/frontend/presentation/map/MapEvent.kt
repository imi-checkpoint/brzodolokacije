package com.example.frontend.presentation.map

//ovde definisemo neke radnje koje korisnici mogu da obave nad mapom
sealed class MapEvent{
    object ToggleLightMap : MapEvent()
}
