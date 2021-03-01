package com.modern.android.formssample.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.modern.android.forms.db.AnswersJson
import com.modern.android.forms.db.AnswersRoomRepository
import com.modern.android.forms.db.DbConverters
import com.modern.android.forms.db.FormJson
import com.modern.android.forms.db.FormRoomRepository

@Database(
    entities = [FormJson::class, AnswersJson::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DbConverters::class)
abstract class FormDatabase : RoomDatabase() {

    companion object {

        const val DB_NAME = "form_db"
    }

    abstract fun answersRepository(): AnswersRoomRepositoryImple

    abstract fun formRepository(): FormRoomRepositoryImple

}

@Dao
abstract class AnswersRoomRepositoryImple: AnswersRoomRepository()


@Dao
abstract class FormRoomRepositoryImple: FormRoomRepository()