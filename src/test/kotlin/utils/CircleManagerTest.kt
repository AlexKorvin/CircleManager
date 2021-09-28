package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import model.Circle
import model.Member
import org.junit.Assert.assertEquals

class CircleManagerTest {

    private lateinit var manager: CircleManager

    @Before
    fun init() {
        manager = CircleManager()
    }

    @Test
    fun testCreatingDuplicateCircles() {
        val frodo = Member(0, "Frodo Baggins")

        val circleId = 0L
        manager.create(circleId, "The Fellowship of the Ring", frodo)
        manager.create(circleId, "Ale Lovers", frodo)

        manager.printCircles()

        assertEquals(2, manager.queryBy(1))
    }

    @Test
    fun testRemovingFromCircle() {
        val boromir = Member(0, "Boromir")

        val fellowshipId = 0L
        val rolesId = 1L

        manager.create(fellowshipId, "The Fellowship of the Ring", boromir)
        manager.create(rolesId, "Sean Bean roles", boromir)

        assertEquals(2, manager.queryBy(1))
        assertEquals(0, manager.queryBy(0))

        manager.remove(boromir, fellowshipId)

        assertEquals(1, manager.queryBy(1))
        assertEquals(0, manager.queryBy(0))
    }

    @Test
    fun testRemovingFromDuplicateCircles() {
        val boromir = Member(0, "Boromir")

        val fellowshipId = 0L
        val rolesId = 0L

        manager.create(fellowshipId, "The Fellowship of the Ring", boromir)
        manager.create(rolesId, "Sean Bean roles", boromir)

        assertEquals(2, manager.queryBy(1))
        assertEquals(0, manager.queryBy(0))

        manager.remove(boromir, fellowshipId)

        assertEquals(0, manager.queryBy(1))
        assertEquals(0, manager.queryBy(0))
    }

    @Test
    fun testDistinctCircles() {
        val boromir = Member(0, "Boromir")
        val sam = Member(1, "Samwise Gamgee")
        val frodo = Member(2, "Frodo Baggins")

        val fellowshipId = 0L
        val rolesId = 1L

        manager.create(fellowshipId, "The Fellowship of the Ring", boromir)
        manager.add(sam, fellowshipId)
        manager.create(rolesId, "Sean Bean roles", boromir)
        manager.create(fellowshipId, "Gandalf birthday party(Secret chat)", frodo)
        manager.add(sam, fellowshipId)
        manager.add(boromir, fellowshipId)

        assertEquals(2, manager.getDistinctCircles().size)
    }

    @Test
    fun testDuplicateCountOf() {
        val boromir = Member(0, "Boromir")
        val sam = Member(1, "Samwise Gamgee")
        val frodo = Member(2, "Frodo Baggins")
        val ned = Member(3, "Eddard Stark")

        val fellowshipId = 0L
        val rolesId = 1L

        val circles = mutableListOf<Circle>()
        val fellowshipCircle = Circle(fellowshipId, "The Fellowship of the Ring").also {
            it.addMember(boromir)
            it.addMember(sam)
            it.addMember(frodo)
        }
        val roleCircle = Circle(rolesId, "Sean Bean roles").also {
            it.addMember(boromir)
            it.addMember(ned)
        }

        circles.add(fellowshipCircle)
        circles.add(roleCircle)
        circles.add(fellowshipCircle)
        circles.add(fellowshipCircle)
        circles.add(roleCircle)

        //Check the count of unique circles
        assertEquals(2, CircleManager.duplicateCountOf(circles))
    }

    @Test
    fun testThreadSafe() {
        val member = Member(0, "Default")

        runBlocking(Dispatchers.Default) {
            for (i in 1..100) {
                launch {
                    delay(5)
                    manager.create(i.toLong(), "Default", member)
                }

                launch {
                    delay(5)
                    manager.remove(member, i.toLong())
                }

                launch {
                    delay(5)
                    manager.queryBy(1)
                }

                launch {
                    delay(5)
                    manager.getDistinctCircles()
                }
            }
        }

        //If CircleManager is not thread-safe java.util.ConcurrentModificationException will happen.
        //No crash = test passed.
        assert(true)
    }
}