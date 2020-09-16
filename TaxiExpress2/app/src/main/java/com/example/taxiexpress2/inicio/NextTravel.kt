package com.example.taxiexpress2.inicio

import com.google.type.LatLng

class NextTravel(val IDViaje: String, val Point: LatLng?, val Tiempo: String){
    constructor(): this ("",null, "")
}