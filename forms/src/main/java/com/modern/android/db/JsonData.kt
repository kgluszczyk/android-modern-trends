package com.modern.android.forms.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.threeten.bp.LocalDate

@Entity(tableName = "forms", primaryKeys = ["id", "form_id"])
data class FormJson(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "form_id") val formId: Long,
    @ColumnInfo(name = "form_date") val formDate: LocalDate,
    @ColumnInfo(name = "json") val json: String
)

@Entity(tableName = "responses", primaryKeys = ["id", "form_id"])
data class AnswersJson(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "form_id") val formId: Long,
    @ColumnInfo(name = "form_date") val formDate: LocalDate,
    @ColumnInfo(name = "json") val json: String
)