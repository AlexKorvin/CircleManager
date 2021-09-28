package utils

import java.security.InvalidParameterException

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object DistanceUtils {

    //Radius in km
    private const val earthRadius = 6371

    //Coordinates are in format (Latitude, Longitude)
    //Returns result in kilometers
    fun getDistanceBetween(
        coordinate1: Pair<Double, Double>,
        coordinate2: Pair<Double, Double>
    ): Double {
        if (!isLatitudeValid(coordinate1.first) || !(isLongitudeValid(coordinate1.second))
            || !isLatitudeValid(coordinate2.first) || !(isLongitudeValid(coordinate2.second))
        ) {
            throw InvalidParameterException("Invalid longitude/latitude value")
        }

        val deltaLatitude = toRadians(coordinate2.first - coordinate1.first)
        val deltaLongitude = toRadians(coordinate2.second - coordinate1.second)

        val a = sin(deltaLatitude / 2f) * sin(deltaLatitude / 2f) +
                cos(toRadians(coordinate1.first)) * cos(toRadians(coordinate2.first)) *
                sin(deltaLongitude / 2f) * sin(deltaLongitude / 2f)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun toRadians(degrees: Double): Double {
        return degrees * (Math.PI / 180.0)
    }

    private fun isLongitudeValid(longitude: Double): Boolean {
        return longitude in -180.0..180.0
    }

    private fun isLatitudeValid(latitude: Double): Boolean {
        return latitude in -90.0..90.0
    }
}