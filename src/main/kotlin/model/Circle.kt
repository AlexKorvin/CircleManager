package model

import utils.DistanceUtils

class Circle constructor(val uniqueId: Long, private val name: String) {

    private var members = mutableListOf<Member>()

    fun addMember(member: Member) {
        if (members.contains(member)) return

        members.add(member)
    }

    fun removeMember(memberId: Long) {
        members.removeAll { it.uniqueId == memberId }
    }

    fun getMemberBy(memberId: Long): Member? {
        return members.find { it.uniqueId == memberId }
    }

    fun getMembersCount(): Int {
        return members.size
    }

    fun isEmpty(): Boolean {
        return members.isEmpty()
    }

    //Coordinates are in format (Latitude, Longitude)
    fun getMembersByDistance(
        distance: Double,
        centerCoordinates: Pair<Double, Double>
    ): List<Member> {
        return members.filter {
            it.coordinates?.let { coordinate ->
                DistanceUtils.getDistanceBetween(
                    centerCoordinates,
                    coordinate
                ) < distance
            } ?: return@filter false
        }
    }

    override fun toString(): String {
        return "Circle(uniqueId=$uniqueId, name='$name', members=$members)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Circle

        if (uniqueId != other.uniqueId) return false

        return true
    }

    override fun hashCode(): Int {
        return uniqueId.hashCode()
    }

}