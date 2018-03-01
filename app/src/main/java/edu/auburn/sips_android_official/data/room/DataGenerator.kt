package edu.auburn.sips_android_official.data.room

import edu.auburn.sips_android_official.data.room.entity.AthleteEntity
import edu.auburn.sips_android_official.data.room.entity.TestDataEntity
import java.util.*
import java.util.ArrayList;
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.HOURS
import javax.xml.datatype.DatatypeConstants.DAYS



/**
 * Created by clj00 on 2/19/2018.
 */
/**
 * Generates data to pre-populate the database
 */
object DataGenerator {

    private val FIRST = arrayOf("Carson", "Bryant", "David", "Jordan", "Ashish")
    private val SECOND = arrayOf("Jones", "Kelley", "Moore", "Peterson", "Gupta")
    private val TEST_DATA = arrayOf("Test Data 1", "Test Data 2", "Test Data 3", "Test Data 4", "Comment 5", "Comment 6")

    fun generateAthletes(): List<AthleteEntity> {
        val athletes = ArrayList<AthleteEntity>(FIRST.size * SECOND.size)
        val rnd = Random()
        for (i in FIRST.indices) {
            for (j in SECOND.indices) {
                val athlete = AthleteEntity(
                        firstName=(FIRST[i]),
                        lastName=(SECOND[j]),
                        email="test@gmail.com",
                        id=(FIRST.size * i + j + 1))
                athletes.add(athlete)
            }
        }
        return athletes
    }
//
//    fun generateTestDataForAthletes(
//            athletes: List<AthleteEntity>): List<TestDataEntity> {
//        val testDataArray = ArrayList<TestDataEntity>()
//        val rnd = Random()
//
//        for (athlete in athletes) {
//            val testDataNumber = rnd.nextInt(5) + 1
//            for (i in 0 until testDataNumber) {
//                val testData = TestDataEntity(
//                        athleteId = (athlete.id),
//                        title = (TEST_DATA[i] + " for " + athlete.name),
//                        testedAt = (Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((testDataNumber - i).toLong()) + TimeUnit.HOURS.toMillis(i.toLong())))
//                )
//                testDataArray.add(testData)
//            }
//        }
//
//        return testDataArray
//    }
}

