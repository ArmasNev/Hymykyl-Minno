package com.example.hymykyl_minno.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Feedback::class, AdminUser::class, SurveyQuestion::class, FeedbackAnswer::class, ServiceOption::class],
    version = 4,
    exportSchema = false
)
abstract class FeedbackDatabase : RoomDatabase() {
    abstract fun feedbackDao(): FeedbackDao
    abstract fun adminUserDao(): AdminUserDao
    abstract fun surveyQuestionDao(): SurveyQuestionDao
    abstract fun feedbackAnswerDao(): FeedbackAnswerDao
    abstract fun serviceOptionDao(): ServiceOptionDao

    companion object {
        @Volatile
        private var Instance: FeedbackDatabase? = null

        fun getDatabase(context: Context): FeedbackDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FeedbackDatabase::class.java, "feedback_database")
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback())
                    .build()
                    .also { Instance = it }
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Instance?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDefaultQuestions(database.surveyQuestionDao())
                        populateDefaultServices(database.serviceOptionDao())
                    }
                }
            }

            private suspend fun populateDefaultQuestions(dao: SurveyQuestionDao) {
                if (dao.getQuestionCount() == 0) {
                    val defaultQuestions = listOf(
                        SurveyQuestion(
                            questionKey = "ACCESSIBILITY",
                            textFi = "Palvelut olivat helposti saatavilla",
                            textEn = "Services were easily accessible",
                            textSv = "Tjänsterna var lättillgängliga",
                            displayOrder = 1
                        ),
                        SurveyQuestion(
                            questionKey = "FLUENCY",
                            textFi = "Palvelukokemus oli mielestäni viihtyisä ja sujuva",
                            textEn = "The service experience was pleasant and smooth",
                            textSv = "Tjänsteupplevelsen var trevlig och smidig",
                            displayOrder = 2
                        ),
                        SurveyQuestion(
                            questionKey = "HEARD",
                            textFi = "Koen saaneeni toiveeni tarvittaessa kuulluksi",
                            textEn = "I feel my wishes were heard if needed",
                            textSv = "Jag känner att mina önskemål blev hörda",
                            displayOrder = 3
                        ),
                        SurveyQuestion(
                            questionKey = "RETURN",
                            textFi = "Haluaisin tulla uudelleen",
                            textEn = "I would like to come again",
                            textSv = "Jag skulle vilja komma igen",
                            displayOrder = 4
                        )
                    )
                    dao.insertQuestions(defaultQuestions)
                }
            }

            private suspend fun populateDefaultServices(dao: ServiceOptionDao) {
                if (dao.getServiceCount() == 0) {
                    val defaultServices = listOf(
                        ServiceOption(
                            serviceKey = "SERVICE_1",
                            textFi = "Suun terveydenhuolto",
                            textEn = "Oral Health Care",
                            textSv = "Munhälsovård",
                            displayOrder = 1
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_2",
                            textFi = "Fysioterapia & osteopatia",
                            textEn = "Physiotherapy & Osteopathy",
                            textSv = "Fysioterapi & osteopati",
                            displayOrder = 2
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_3",
                            textFi = "Senioripalvelut",
                            textEn = "Senior Services",
                            textSv = "Seniortjänster",
                            displayOrder = 3
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_4",
                            textFi = "Perhepalvelut",
                            textEn = "Family Services",
                            textSv = "Familjetjänster",
                            displayOrder = 4
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_5",
                            textFi = "Aikuisneuvola",
                            textEn = "Adult Health Clinic",
                            textSv = "Vuxenrådgivning",
                            displayOrder = 5
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_6",
                            textFi = "Toimintaterapia",
                            textEn = "Occupational Therapy",
                            textSv = "Arbetsterapi",
                            displayOrder = 6
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_7",
                            textFi = "Apuvälinepalvelut",
                            textEn = "Assistive Device Services",
                            textSv = "Hjälpmedelstjänster",
                            displayOrder = 7
                        ),
                        ServiceOption(
                            serviceKey = "SERVICE_8",
                            textFi = "Matalan kynnyksen palvelut",
                            textEn = "Low-Threshold Services",
                            textSv = "Lågsköskeltjänster",
                            displayOrder = 8
                        )
                    )
                    dao.insertServices(defaultServices)
                }
            }
        }
    }
}
