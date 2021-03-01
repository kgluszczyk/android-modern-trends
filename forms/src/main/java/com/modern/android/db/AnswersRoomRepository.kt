package com.modern.android.forms.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.repository.AnswersRepository
import com.squareup.moshi.Moshi
import org.threeten.bp.LocalDate

@Dao
abstract class AnswersRoomRepository : AnswersRepository {

    private val jsonAdapter = Moshi.Builder().build().adapter(FormAnswers::class.java)

    override fun save(context: FormContext, answers: FormAnswers) =
        save(
            AnswersJson(
                id = context.id,
                formId = answers.formId,
                formDate = context.date,
                json = jsonAdapter.toJson(answers)
            )
        )

    override fun delete(context: FormContext, formId: Long) {
        deleteByIds(context.id, formId)
    }

    override fun getForForm(context: FormContext, form: Form): FormAnswers? {
        val stored = queryByIds(id = context.id, formId = form.id) ?: return null
        return jsonAdapter.fromJson(stored.json)
    }

    override fun deleteOlderThan(date: LocalDate) = deleteByDate(date)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(answers: AnswersJson)

    @Query("SELECT * FROM responses WHERE id = :id AND form_id = :formId")
    abstract fun queryByIds(id: String, formId: Long): AnswersJson?

    @Query("DELETE FROM responses WHERE id = :id AND form_id = :formId")
    abstract fun deleteByIds(id: String, formId: Long)

    @Query("DELETE FROM responses WHERE form_date < :date")
    abstract fun deleteByDate(date: LocalDate)
}