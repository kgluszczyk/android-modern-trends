package com.modern.android.formssample.di

import android.content.Context
import androidx.room.Room
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository
import com.modern.android.formssample.db.FormDatabase
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {
    lateinit var db: FormDatabase

    fun init(context: Context) {
        if (::db.isInitialized) {
            return
        }

        db = Room.databaseBuilder(context, FormDatabase::class.java, FormDatabase.DB_NAME)
            .build()
    }

    @Provides
    fun provideDatabase() = db

    @Provides
    fun provideAnswersRepository(db: FormDatabase): AnswersRepository = db.answersRepository()

    @Provides
    fun provideFormRepository(db: FormDatabase): FormRepository = db.formRepository()

}