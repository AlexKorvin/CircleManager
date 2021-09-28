package model

data class Member(val uniqueId: Long, val name: String) {
    //Coordinates are in format (Latitude, Longitude)
    var coordinates: Pair<Double, Double>? = null
}