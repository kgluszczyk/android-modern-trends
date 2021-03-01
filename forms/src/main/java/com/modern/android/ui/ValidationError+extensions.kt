package com.modern.android.forms.ui

import com.modern.android.forms.entity.ValidationError

fun List<ValidationError>.findErrorMessage(id: Long) = firstOrNull { it.id == id }?.message
