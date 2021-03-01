package com.modern.android.forms.ui.page

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormItemCheckboxBinding
import com.modern.android.forms.databinding.FormItemDropdownBinding
import com.modern.android.forms.databinding.FormItemErrorPlaceholderBinding
import com.modern.android.forms.databinding.FormItemHeader2Binding
import com.modern.android.forms.databinding.FormItemHeader3Binding
import com.modern.android.forms.databinding.FormItemHeaderBinding
import com.modern.android.forms.databinding.FormItemInputBinding
import com.modern.android.forms.databinding.FormItemRearrangeBinding
import com.modern.android.forms.databinding.FormItemRearrangeListBinding
import com.modern.android.forms.databinding.FormItemRowBinding
import com.modern.android.forms.databinding.FormItemSectionBinding
import com.modern.android.forms.databinding.FormItemToggleBinding
import com.modern.android.forms.databinding.FormItemValueBinding
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.ui.DropdownBottomSheetDialog
import com.modern.android.forms.ui.ItemMoveCallback
import com.modern.android.forms.ui.ifValidPosition
import com.modern.commons.android.list2.BindingViewHolder2
import com.modern.commons.android.list2.ListItem2
import com.modern.commons.android.list2.ViewHolder2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SectionViewHolder(binding: FormItemSectionBinding) :
    BindingViewHolder2<ItemSection, FormItemSectionBinding>(binding) {

    override fun bind(item: ItemSection) {
        binding.header.text = item.title
    }
}

class HeaderViewHolder(binding: FormItemHeaderBinding) :
    BindingViewHolder2<ItemHeader, FormItemHeaderBinding>(binding) {

    override fun bind(item: ItemHeader) {
        binding.header.text = item.title
    }
}

class Header2ViewHolder(binding: FormItemHeader2Binding) :
    BindingViewHolder2<ItemHeader2, FormItemHeader2Binding>(binding) {

    override fun bind(item: ItemHeader2) {
        binding.header.text = item.title
    }
}

class Header3ViewHolder(binding: FormItemHeader3Binding) :
    BindingViewHolder2<ItemHeader3, FormItemHeader3Binding>(binding) {

    override fun bind(item: ItemHeader3) {
        binding.header.text = item.title
    }
}

class CheckboxViewHolder(
    binding: FormItemCheckboxBinding,
    private val listener: OnValueChangedListener
) : BindingViewHolder2<ItemInputCheckbox, FormItemCheckboxBinding>(binding) {

    private val errorHelper = ErrorUIHelper(binding.errorMessage, binding.border, binding.label)

    init {
        binding.root.setSafeOnClickListener { binding.checkbox.performClick() }
    }

    override fun bind(item: ItemInputCheckbox) {
        binding.checkbox.setOnCheckedChangeListener(null)
        errorHelper.updateError(item.errorMessage)
        binding.label.text = item.label
        binding.subline.text = item.subtitle
        binding.subline.isVisible = item.subtitle.isNotEmpty()
        binding.checkbox.isChecked = item.checked
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            ifValidPosition {
                errorHelper.clearError()
                listener.onValueChanged(
                    ItemAnswer(
                        questionId = item.inputId,
                        answer = isChecked.toString(),
                        type = ItemAnswer.AnswerType.BOOLEAN
                    )
                )
            }
        }
    }
}

class ToggleViewHolder(
    binding: FormItemToggleBinding,
    private val listener: OnValueChangedListener
) : BindingViewHolder2<ItemInputToggle, FormItemToggleBinding>(binding) {

    private val errorHelper = ErrorUIHelper(binding.errorMessage, binding.border, binding.label)

    override fun bind(item: ItemInputToggle) {
        binding.root.setSafeOnClickListener { binding.toggle.performClick() }
        binding.toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != item.checked) {
                ifValidPosition {
                    errorHelper.clearError()
                    listener.onValueChanged(
                        ItemAnswer(
                            questionId = item.inputId,
                            answer = isChecked.toString(),
                            type = ItemAnswer.AnswerType.BOOLEAN
                        )
                    )
                }
            }
        }

        errorHelper.updateError(item.errorMessage)
        binding.label.text = item.title
        binding.subline.text = item.subTitle
        binding.subline.isVisible = item.subTitle.isNotEmpty()
        binding.toggle.isChecked = item.checked
    }
}

class InputViewHolder(
    binding: FormItemInputBinding,
    private val listener: OnValueChangedListener
) : BindingViewHolder2<ItemInput, FormItemInputBinding>(binding) {

    private val errorHelper = ErrorUIHelper(binding.errorMessage, binding.border, binding.input)
    private val compositeDisposable = CompositeDisposable()

    init {
        binding.input.setOnEditorActionListener { v, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_NEXT) return@setOnEditorActionListener false

            binding.root.findNextFocus(binding.input)?.requestFocus() ?: v.hideKeyboard()
            return@setOnEditorActionListener true
        }
        binding.input.setSafeOnClickListener { binding.input.showKeyboard() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }

    override fun bind(item: ItemInput) {
        errorHelper.updateError(item.errorMessage)

        binding.label.text = item.label
        binding.label.isVisible = item.label.isNotEmpty()
        binding.input.apply {
            if (item.value != text.toString()) {
                setText(item.value)
            }
            hint = item.hint
            inputType = when (item.inputType) {
                ItemInput.InputType.TEXT -> InputType.TYPE_CLASS_TEXT
                ItemInput.InputType.NUMBER -> InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            setSelection(text.length)
            onDetachedFromWindow()
            textChanges()
                .skipInitialValue()
                .debounce(5L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ifValidPosition {
                        errorHelper.clearError()
                        println("Text changed!")
                        listener.onValueChanged(
                            ItemAnswer(
                                questionId = item.inputId,
                                answer = text.toString(),
                                type = when (item.inputType) {
                                    ItemInput.InputType.TEXT -> ItemAnswer.AnswerType.STRING
                                    ItemInput.InputType.NUMBER -> ItemAnswer.AnswerType.NUMBER
                                }
                            )
                        )
                    }
                }
        }
    }

    private fun View.findNextFocus(input: EditText): View? {
        val parent = parent?.parent as? View ?: return null
        return parent.getFocusables(FOCUS_DOWN)
            .dropWhile { it != input }
            .drop(1)
            .firstOrNull()
            ?.takeIf { it is EditText }
    }

    private fun View.hideKeyboard() =
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(windowToken, 0) == true

    private fun View.showKeyboard() =
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, SHOW_IMPLICIT) == true

}

class DropdownViewHolder(
    binding: FormItemDropdownBinding,
    private val listener: OnValueChangedListener
) : BindingViewHolder2<ItemDropdown, FormItemDropdownBinding>(binding) {

    private val errorHelper = ErrorUIHelper(binding.errorMessage, binding.border, binding.input)

    override fun bind(item: ItemDropdown) {
        errorHelper.updateError(item.errorMessage)
        with(binding) {
            label.text = item.title
            input.text = item.value
            root.setSafeOnClickListener { showBottomSheet(item) }
        }
    }

    private fun showBottomSheet(item: ItemDropdown) {
        DropdownBottomSheetDialog(context, item.items, binding.input.text.toString()) {
            errorHelper.clearError()
            binding.input.text = it
            listener.onValueChanged(
                ItemAnswer(
                    questionId = item.inputId,
                    answer = it,
                    type = ItemAnswer.AnswerType.STRING
                )
            )
        }.show()
    }
}

class ValueViewHolder(binding: FormItemValueBinding) :
    BindingViewHolder2<ItemText, FormItemValueBinding>(binding) {

    override fun bind(item: ItemText) {
        binding.label.text = item.title
        binding.label.isVisible = item.title.isNotEmpty()
        binding.value.text = item.value
        binding.value.isVisible = item.value.isNotEmpty()
    }
}

class RowViewHolder(
    binding: FormItemRowBinding,
    pool: RecyclerView.RecycledViewPool,
    listener: OnValueChangedListener
) : BindingViewHolder2<ItemRow, FormItemRowBinding>(binding) {

    private val formAdapter by lazy { FormAdapter(listener) }

    init {
        binding.recycler.apply {
            setRecycledViewPool(pool)
            adapter = formAdapter
        }
    }

    override fun bindChange(oldItem: ItemRow, newItem: ItemRow) {
        if (oldItem.items.size != newItem.items.size) {
            bind(newItem)
        } else {
            formAdapter.items = newItem.items
        }
    }

    override fun bind(item: ItemRow) {
        binding.recycler.layoutManager = GridLayoutManager(context, item.items.size)
        formAdapter.items = item.items
    }
}

class RearrangeItemViewHolder(binding: FormItemRearrangeBinding) :
    BindingViewHolder2<ItemRearrangeListItem, FormItemRearrangeBinding>(binding) {

    override fun bind(item: ItemRearrangeListItem) {
        binding.godCodeRank.text = item.value
        binding.number.text = (item.position + 1).toString()
    }
}

class RearrangeItemListViewHolder(
    binding: FormItemRearrangeListBinding,
    pool: RecyclerView.RecycledViewPool,
    listener: OnValueChangedListener
) : BindingViewHolder2<ItemRearrangeList, FormItemRearrangeListBinding>(binding) {

    private val rearrangeFormAdapter: RearrangeFormAdapter = RearrangeFormAdapter(
        listener
    )

    init {
        val itemTouchHelper = ItemTouchHelper(ItemMoveCallback(rearrangeFormAdapter))
        rearrangeFormAdapter.startDragListener = { itemTouchHelper.startDrag(it) }
        binding.recycler.apply {
            setRecycledViewPool(pool)
            itemTouchHelper.attachToRecyclerView(this)
            adapter = rearrangeFormAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun bind(item: ItemRearrangeList) {
        binding.header.text = item.title
        rearrangeFormAdapter.items = item.items
    }
}

class ErrorPlaceholderViewHolder(
    binding: FormItemErrorPlaceholderBinding,
    private val recyclerView: RecyclerView
) : BindingViewHolder2<ItemErrorPlaceholder, FormItemErrorPlaceholderBinding>(binding) {

    private var viewHolder: RecyclerView.ViewHolder? = null

    override fun bind(item: ItemErrorPlaceholder) {
        val differentItemTypes = viewHolder?.itemViewType != item.item.itemType
        if (differentItemTypes) {
            viewHolder?.let { recyclerView.recycledViewPool.putRecycledView(it) }
            viewHolder = recyclerView.recycledViewPool.getRecycledView(item.item.itemType)
                ?: recyclerView.adapter?.createViewHolder(recyclerView, item.item.itemType)
        }

        val bindViewHolder = viewHolder as? ViewHolder2<ListItem2> ?: return
        if (differentItemTypes) {
            binding.container.removeAllViews()
            binding.container.addView(bindViewHolder.itemView)
        }
        binding.errorMessage.text = item.largestError
        bindViewHolder.bind(item.item)
    }
}

class ErrorUIHelper(val errorMessage: TextView, vararg val views: View) {

    private val defaultColors = views.map { it.color }
    private val errorColor = ContextCompat.getColor(errorMessage.context, R.color.form_carnation)

    fun clearError() {
        if (!errorMessage.isVisible) return
        errorMessage.isInvisible = true
        views.forEachIndexed { index, view ->
            view.color = defaultColors[index]
        }
    }

    fun updateError(message: String?) {
        errorMessage.text = message
        errorMessage.isVisible = !message.isNullOrBlank()
        views.forEachIndexed { index, view ->
            view.color = if (message == null) defaultColors[index] else errorColor
        }
    }

    @get:ColorInt
    private var View.color
        get() = when (this) {
            is EditText -> currentHintTextColor
            is TextView -> currentTextColor
            else -> (background as? ColorDrawable)?.color ?: Color.TRANSPARENT
        }
        set(value) = when (this) {
            is EditText -> setHintTextColor(value)
            is TextView -> setTextColor(value)
            else -> setBackgroundColor(value)
        }
}