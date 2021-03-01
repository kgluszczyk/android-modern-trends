package com.modern.android.forms.entity

import android.os.Parcelable
import com.modern.android.commons.predicate.Criterion
import com.modern.android.forms.entity.Type.HEADER
import com.modern.android.forms.entity.Type.HEADER2
import com.modern.android.forms.entity.Type.HEADER3
import com.modern.android.forms.entity.Type.INPUT_CHECKBOX
import com.modern.android.forms.entity.Type.INPUT_DROPDOWN
import com.modern.android.forms.entity.Type.INPUT_NUMERIC
import com.modern.android.forms.entity.Type.INPUT_TEXT
import com.modern.android.forms.entity.Type.INPUT_TOGGLE
import com.modern.android.forms.entity.Type.REARRANGE_LIST
import com.modern.android.forms.entity.Type.REARRANGE_LIST_ITEM
import com.modern.android.forms.entity.Type.ROW
import com.modern.android.forms.entity.Type.TEXT
import com.modern.commons.predicate.registerCriterion
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Form(
    val id: Long,
    val title: String,
    val pages: List<Page>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Page(
    val title: String,
    val items: List<Item>
) : Parcelable

sealed class Item(val type: Type) : Parcelable

@Parcelize
data class FormValidation(
    val errorMessage: String,
    val criteria: Criterion
) : Parcelable

sealed class Input(type: Type) : Item(type) {
    abstract val id: Long
    abstract val value: String?
    open val mandatory: Boolean = false
    open val validation: FormValidation? = null
}

@Parcelize
@JsonClass(generateAdapter = true)
data class Header(val title: String) : Item(HEADER)

@Parcelize
@JsonClass(generateAdapter = true)
data class Header2(val title: String) : Item(HEADER2)

@Parcelize
@JsonClass(generateAdapter = true)
data class Header3(val title: String) : Item(HEADER3)

@Parcelize
@JsonClass(generateAdapter = true)
data class InputCheckbox(
    override val id: Long,
    val title: String?,
    val subtitle: String?,
    override val value: String?,
    override val mandatory: Boolean = false,
    override val validation: FormValidation? = null
) : Input(INPUT_CHECKBOX)

@Parcelize
@JsonClass(generateAdapter = true)
data class InputText(
    override val id: Long,
    val title: String?,
    val hint: String?,
    override val value: String?,
    override val mandatory: Boolean = false,
    override val validation: FormValidation? = null
) : Input(INPUT_TEXT)

@Parcelize
@JsonClass(generateAdapter = true)
data class InputNumeric(
    override val id: Long,
    val title: String?,
    val hint: String?,
    override val value: String?,
    override val mandatory: Boolean = false,
    override val validation: FormValidation? = null
) : Input(INPUT_NUMERIC)

@Parcelize
@JsonClass(generateAdapter = true)
data class InputDropdown(
    override val id: Long,
    val title: String?,
    override val value: String?,
    override val items: List<Item>,
    override val mandatory: Boolean = false,
    override val validation: FormValidation? = null
) : Input(INPUT_DROPDOWN), CompoundItem<Item>

@Parcelize
@JsonClass(generateAdapter = true)
data class Text(
    val title: String?,
    val value: String?,
    val calculation: Criterion? = null
) : Item(TEXT)

@Parcelize
@JsonClass(generateAdapter = true)
data class Toggle(
    override val id: Long,
    val title: String,
    val subtitle: String?,
    override val value: String?,
    override val mandatory: Boolean = false,
    override val validation: FormValidation? = null
) : Input(INPUT_TOGGLE)

@Parcelize
@JsonClass(generateAdapter = true)
data class RearrangeList(val title: String, override val items: List<RearrangeListItem>) : Item(REARRANGE_LIST),
    CompoundItem<RearrangeListItem>

@Parcelize
@JsonClass(generateAdapter = true)
data class RearrangeListItem(override val id: Long, val title: String, override val value: String) :
    Input(REARRANGE_LIST_ITEM)

@Parcelize
@JsonClass(generateAdapter = true)
data class Row(override val items: List<Item>) : Item(ROW), CompoundItem<Item>

interface CompoundItem<T : Item> {
    val items: List<T>
}

enum class Type(val typeName: String) {
    HEADER("HEADER"),
    HEADER2("HEADER_2"),
    HEADER3("HEADER_3"),
    TEXT("TEXT"),
    INPUT_CHECKBOX("INPUT_CHECKBOX"),
    INPUT_NUMERIC("INPUT_NUMERIC"),
    INPUT_TEXT("INPUT_TEXT"),
    INPUT_TOGGLE("INPUT_TOGGLE"),
    INPUT_DROPDOWN("INPUT_DROPDOWN"),
    REARRANGE_LIST("REARRANGE_LIST"),
    REARRANGE_LIST_ITEM("REARRANGE_LIST_ITEM"),
    ROW("ROW")
}

fun List<Item>.flatten() = flatMap {
    if (it is CompoundItem<*>) it.items else listOf(it)
}

fun createFormAdapter(): JsonAdapter<Form> = Moshi.Builder()
    .registerFormAdapterFactory()
    .build()
    .adapter(Form::class.java)

fun Moshi.Builder.registerFormAdapterFactory(): Moshi.Builder = registerCriterion()
    .add(FormValidationAdapter())
    .add(
        PolymorphicJsonAdapterFactory.of(Item::class.java, "type")
            .withSubtype(Header::class.java, HEADER.typeName)
            .withSubtype(Header2::class.java, HEADER2.typeName)
            .withSubtype(Header3::class.java, HEADER3.typeName)
            .withSubtype(InputDropdown::class.java, INPUT_DROPDOWN.typeName)
            .withSubtype(InputCheckbox::class.java, INPUT_CHECKBOX.typeName)
            .withSubtype(InputNumeric::class.java, INPUT_NUMERIC.typeName)
            .withSubtype(InputText::class.java, INPUT_TEXT.typeName)
            .withSubtype(Text::class.java, TEXT.typeName)
            .withSubtype(Toggle::class.java, INPUT_TOGGLE.typeName)
            .withSubtype(RearrangeList::class.java, REARRANGE_LIST.typeName)
            .withSubtype(RearrangeListItem::class.java, REARRANGE_LIST_ITEM.typeName)
            .withSubtype(Row::class.java, ROW.typeName)
    )

private const val ERROR_MESSAGE_KEY = "errorMessage"

class FormValidationAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<Criterion>): FormValidation? {
        val criterion = jsonReader.peekJson().use { copyReader ->
            delegate.fromJson(copyReader)
        } ?: return null
        val jsonMap = (jsonReader.readJsonValue() as? Map<String, Any>) ?: emptyMap()
        val errorMessage = jsonMap[ERROR_MESSAGE_KEY]?.toString() ?: ""
        return FormValidation(errorMessage, criterion)
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: FormValidation?, delegate: JsonAdapter<Criterion>) {
        if (value == null) {
            writer.nullValue()
            return
        }

        writer.beginObject()
        val token = writer.beginFlatten()
        delegate.toJson(writer, value.criteria)
        writer.endFlatten(token)
        writer.name(ERROR_MESSAGE_KEY)
        writer.value(value.errorMessage)
        writer.endObject()
    }
}