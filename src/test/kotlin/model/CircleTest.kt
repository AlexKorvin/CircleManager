package model

import org.junit.Assert.*
import org.junit.Test

class CircleTest {

    @Test
    fun testUniqueMembersCase() {
        val frodo = Member(1, "Frodo")
        val circle = Circle(1, "Ale Lovers")

        assertTrue(circle.isEmpty())

        circle.addMember(frodo)
        circle.addMember(frodo)

        assertEquals(1, circle.getMembersCount())
        assertFalse(circle.isEmpty())
    }

    @Test
    fun testAddingRemovingCase() {
        val frodo = Member(1, "Frodo")
        val sam = Member(2, "Sam")
        val gandalf = Member(3, "Gandalf")

        val circle = Circle(1, "Ale Lovers")

        circle.addMember(frodo)
        circle.addMember(sam)

        assertEquals(2, circle.getMembersCount())

        circle.removeMember(gandalf.uniqueId)
        assertEquals(2, circle.getMembersCount())

        circle.removeMember(sam.uniqueId)
        assertEquals(1, circle.getMembersCount())
    }

    @Test
    fun testMemberBy() {
        val frodo = Member(1, "Frodo")
        val sam = Member(2, "Sam")
        val gandalf = Member(3, "Gandalf")

        val circle = Circle(1, "Ale Lovers")

        circle.addMember(frodo)
        circle.addMember(sam)

        assertEquals(null, circle.getMemberBy(gandalf.uniqueId))
        assertEquals(frodo, circle.getMemberBy(frodo.uniqueId))
    }

    @Test
    fun testMembersByDistance() {
        val frodo = Member(1, "Frodo").also {
            it.coordinates = Pair(50.450001, 30.523333)
        }
        val sam = Member(2, "Sam").also {
            it.coordinates = Pair(50.44, 30.56)
        }
        val gandalf = Member(3, "Gandalf").also {
            it.coordinates = Pair(48.85341, 2.3488)
        }

        val circle = Circle(1, "Ale Lovers")

        circle.addMember(frodo)
        circle.addMember(sam)
        circle.addMember(gandalf)

        val mordorCoordinates = Pair(55.751244, 37.618423)

        val closestToMordorMembers = circle.getMembersByDistance(1000.0, mordorCoordinates)
        assertEquals(2, closestToMordorMembers.size)
        assertTrue(closestToMordorMembers.contains(frodo))
        assertTrue(closestToMordorMembers.contains(sam))
        assertFalse(closestToMordorMembers.contains(gandalf))

    }
}