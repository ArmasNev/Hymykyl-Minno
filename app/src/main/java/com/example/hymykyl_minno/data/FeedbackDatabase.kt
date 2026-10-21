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
    entities = [Feedback::class, AdminUser::class, SurveyQuestion::class, FeedbackAnswer::class],
    version = 3,
    exportSchema = false
)
abstract class FeedbackDatabase : RoomDatabase() {
    abstract fun feedbackDao(): FeedbackDao
    abstract fun adminUserDao(): AdminUserDao
    abstract fun surveyQuestionDao(): SurveyQuestionDao
    abstract fun feedbackAnswerDao(): FeedbackAnswerDao

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
        }
    }
}
