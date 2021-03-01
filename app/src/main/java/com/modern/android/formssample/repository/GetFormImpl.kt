package com.modern.android.formssample.repository

import android.content.Context
import com.modern.android.forms.domain.GetForm
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.entity.registerFormAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class GetFormImpl constructor(private val appContext: Context) : GetForm {

    override fun execute(context: FormContext): Form {
        val moshi = Moshi.Builder()
            .registerFormAdapterFactory()
            .add(KotlinJsonAdapterFactory())
            .build()

        val listType = Types.newParameterizedType(List::class.java, Form::class.java)
        val adapter: JsonAdapter<List<Form>> = moshi.adapter(listType)
        val file = "form.json"
        val myjson = appContext.assets.open(file).bufferedReader().use { it.readText() }
        return adapter.fromJson(myjson)!!.first()
    }
}