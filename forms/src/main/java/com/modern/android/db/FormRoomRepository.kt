package com.modern.android.forms.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.entity.createFormAdapter
import com.modern.android.forms.repository.FormRepository
import org.threeten.bp.LocalDate

@Dao
abstract class FormRoomRepository : FormRepository {

    private val jsonAdapter = createFormAdapter()

    @Transaction
    override fun replaceWithinContext(context: FormContext, form: Form): Long {
        val dbEntity = FormJson(
            id = context.id,
            formId = form.id,
            formDate = context.date,
            json = jsonAdapter.toJson(form)
        )
        deleteByContext(context.id)
        save(dbEntity)
        return form.id
    }

    override fun deleteByContext(context: FormContext, formId: Long) {
        deleteByContext(context.id, formId)
    }

    override fun getByContext(context: FormContext): Form? {
        val formJson = queryByContext(context.id) ?: return null
        return jsonAdapter.fromJson(formJson.json)
    }

    override fun deleteOlderThan(date: LocalDate) = deleteByDate(date)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun save(formJson: FormJson): Long

    @Query("SELECT * FROM forms WHERE id IS :id")
    abstract fun queryByContext(id: String): FormJson?

    @Query("DELETE FROM forms WHERE id IS :id")
    abstract fun deleteByContext(id: String)

    @Query("DELETE FROM forms WHERE id IS :id AND form_id IS :formId")
    abstract fun deleteByContext(id: String, formId: Long)

    @Query("DELETE FROM forms WHERE form_date < :date")
    abstract fun deleteByDate(date: LocalDate)
}