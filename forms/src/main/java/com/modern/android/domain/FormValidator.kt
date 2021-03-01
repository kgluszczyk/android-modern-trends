package com.modern.android.forms.domain

import android.content.Context
import com.modern.android.forms.R
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.Input
import com.modern.android.forms.entity.InputCheckbox
import com.modern.android.forms.entity.InputNumeric
import com.modern.android.forms.entity.Toggle
import com.modern.android.forms.entity.ValidationError
import com.modern.android.forms.entity.flatten
import com.modern.android.forms.fromApiBoolean
import com.modern.android.validateMandatoryFields
import com.modern.commons.predicate.Predicate

class FormValidator constructor(private val context: Context) : Validator {

    private val paramsRegex = "(\\$[0-9]+)".toRegex()

    override fun execute(form: Form): List<ValidationError> {
        val inputs = form.toInputs()
        return inputs.validateInputs(inputs.toParams())
    }

    override fun execute(questionIds: List<Long>, contextForm: Form): List<ValidationError> {
        val contextFormInputs = contextForm.toInputs()
        val inputs = contextFormInputs.filter { it.id in questionIds }
        return inputs.validateInputs(contextFormInputs.toParams())
    }

    private fun List<Input>.validateInputs(parameters: Map<String, String>): List<ValidationError> {
        return validateMandatoryFields() + validateValidators(parameters) + validateDecimals()
    }

    private fun List<Input>.validateDecimals(): List<ValidationError> {
        val separators = listOf('.', ',')
        return filterIsInstance<InputNumeric>()
            .mapNotNull { input -> input.value?.let { Pair(input.value, input.id) } }
            .filter { (value, _) ->
                value.isNotEmpty() &&
                        (separators.contains(value.first()) || separators.contains(value.last()))
            }.map { (_, id) ->
                ValidationError(
                    id = id,
                    message = context.getString(R.string.form_invalid_value)
                )
            }
    }

    private fun List<Input>.validateValidators(parameters: Map<String, String>): List<ValidationError> {
        val predicate = Predicate(parameters)
        return filter { it.validation != null }
            .filter {
                val validator = it.validation?.criteria ?: return@filter false
                !predicate.evaluateBoolean(validator)
            }
            .map {
                ValidationError(
                    id = it.id,
                    message = it.validation?.errorMessage?.resolveParams(parameters)
                        ?: context.getString(R.string.form_invalid_value)
                )
            }
    }

    private fun Input.getValue(): String =
        when (this) {
            is InputCheckbox, is Toggle -> if (value?.toBoolean() == true) "Yes" else "No"
            is InputNumeric -> value.orEmpty().ifBlank { "0" }
            else -> value.orEmpty()
        }

    private fun String.resolveParams(parameters: Map<String, String>): String {
        return paramsRegex.findAll(this).fold(this) { acc, value ->
            val param = value.groups[1]?.value ?: return@fold acc
            val fieldId = param.substring(1)
            return@fold acc.replace(param, parameters[fieldId] ?: param)
        }
    }

    private fun List<Input>.toParams(): Map<String, String> = map { input -> input.id.toString() to input.getValue() }
        .toMap()

    private fun Form.toInputs(): List<Input> = pages.flatMap { it.items }
        .flatten()
        .filterIsInstance<Input>()
}