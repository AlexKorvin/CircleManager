package utils

import org.junit.Assert.*
import org.junit.Test
import java.security.InvalidParameterException

class DistanceUtilsTest {

    companion object {
        const val DISTANCE_DELTA = 1.0
    }

    @Test
    fun testCorrectResult() {
        val parisCoordinates = Pair(48.85341, 2.3488)
        val kyivCoordinates = Pair(50.450001, 30.523333)

        val correctDistance = 2023.71

        assertEquals(
            correctDistance,
            DistanceUtils.getDistanceBetween(parisCoordinates, kyivCoordinates),
            DISTANCE_DELTA
        )
    }

    @Test
    fun testCorrectEdgeCase() {
        val somePointCoordinates = Pair(-90.0, 180.0)
        val kyivCoordinates = Pair(50.450001, 30.523333)

        val correctDistance = 15616.58

        assertEquals(
            DistanceUtils.getDistanceBetween(somePointCoordinates, kyivCoordinates),
            correctDistance,
            DISTANCE_DELTA
        )
    }

    @Test
    fun testSameCoordinateCase() {
        val kyivCoordinates = Pair(50.450001, 30.523333)

        val correctDistance = 0.0

        assertEquals(
            DistanceUtils.getDistanceBetween(kyivCoordinates, kyivCoordinates),
            correctDistance,
            DISTANCE_DELTA
        )
    }

    @Test(expected = InvalidParameterException::class)
    fun testOutOfBoundsCase1() {
        val somePointCoordinates = Pair(-90.7, 180.0)
        val kyivCoordinates = Pair(50.450001, 30.523333)

        DistanceUtils.getDistanceBetween(somePointCoordinates, kyivCoordinates)
    }

    @Test(expected = InvalidParameterException::class)
    fun testOutOfBoundsCase2() {
        val somePointCoordinates = Pair(90.0, 180.1)
        val kyivCoordinates = Pair(50.450001, 30.523333)

        DistanceUtils.getDistanceBetween(kyivCoordinates, somePointCoordinates)
    }
}