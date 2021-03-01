package com.modern.android

import com.modern.android.forms.entity.Input
import com.modern.android.forms.entity.InputCheckbox
import com.modern.android.forms.entity.Toggle
import com.modern.android.forms.entity.ValidationError
import com.modern.android.forms.fromApiBoolean

//todo imperative vs functional
fun List<Input>.validateMandatoryFields(): List<ValidationError> {
    val result = mutableListOf<Input>()
    for (i in indices) {
        if (this[i].mandatory) {
            result.add(this[i])
        }
    }
    val result2 = mutableListOf<Input>()
    for (i in result.indices) {
        if (result[i] is InputCheckbox || result[i] is Toggle) {
            if (result[i].value?.fromApiBoolean() != true) {
                result2.add(this[i])
            }
        } else if (result[i].value.isNullOrBlank()) {
            result2.add(this[i])
        }
    }
    val result3 = mutableListOf<ValidationError>()
    for (i in result2.indices) {
        result3.add(ValidationError(result2[i].id, "Required"))
    }
    return result3
}
