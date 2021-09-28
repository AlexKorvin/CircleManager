package utils

import model.Circle
import model.Member
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap

class CircleManager {

    private var circles = CopyOnWriteArrayList<Circle>()

    /*To provide O(1) time complexity for queryBy method we need this hashmap.
      In real life, there is no sense for such "optimization".
      QueryBy is not an often-used method.
      The key is members count. Value is the number of circles with such members count. */
    private val countHolder = HashMap<Int, Int>()

    fun create(circleId: Long, name: String = "Unnamed", member: Member) {
        Circle(circleId, name).also {
            it.addMember(member)
            circles.add(it)

            incrementCircleCountFor(1)
        }
    }

    fun add(member: Member, circleId: Long) {
        val appropriateCircles = circles.filter { it.uniqueId == circleId }
        if (appropriateCircles.isEmpty()) {
            create(circleId, member = member)
        } else {
            appropriateCircles.forEach {
                val membersCount = it.getMembersCount()
                it.addMember(member)

                decrementCircleCountFor(membersCount)
                incrementCircleCountFor(membersCount + 1)
            }
        }
    }

    fun remove(member: Member, circleId: Long) {
        circles.filter { it.uniqueId == circleId }.forEach {
            val membersCount = it.getMembersCount()
            it.removeMember(member.uniqueId)
            if (it.isEmpty()) {
                circles.remove(it)
            }

            decrementCircleCountFor(membersCount)
            incrementCircleCountFor(membersCount - 1)
        }
    }

    fun queryBy(membersCount: Int): Int {
        return countHolder[membersCount] ?: 0
    }

    fun getDistinctCircles(): List<Circle> {
        circles.sortWith { circle1, circle2 ->
            -circle1.getMembersCount().compareTo(circle2.getMembersCount())
        }

        return circles.distinct()
    }

    fun printCircles() {
        circles.forEach { println(it) }
    }

    private fun incrementCircleCountFor(membersCount: Int) {
        if (membersCount == 0) return

        val circlesCount = countHolder[membersCount] ?: 0
        countHolder[membersCount] = circlesCount + 1
    }

    private fun decrementCircleCountFor(membersCount: Int) {
        if (membersCount == 0) return

        val circlesCount = countHolder[membersCount] ?: 0
        countHolder[membersCount] = circlesCount - 1
    }

    companion object {
        fun duplicateCountOf(circleList: List<Circle>) : Int {
            return circleList.distinct().count { Collections.frequency(circleList, it) > 1 }
        }
    }
}