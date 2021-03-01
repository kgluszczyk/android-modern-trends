package com.modern.android.forms.ui.page

import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormItemRearrangeBinding
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.ui.ItemTouchHelperContract
import com.modern.commons.android.list2.Adapter2
import com.modern.commons.android.list2.ViewHolderFactory2

class RearrangeFormAdapter(
    private val listener: OnValueChangedListener
) : Adapter2<ItemRearrangeListItem>(),
    ItemTouchHelperContract {

    private var fromInitialPosition: Int = -1
    private var fromFinalPosition: Int = -1
    var startDragListener: (RecyclerView.ViewHolder) -> Unit = {}

    override val factory: ViewHolderFactory2<ItemRearrangeListItem> = { layoutInflater, viewGroup, viewType ->
        when (viewType) {
            R.layout.form_item_rearrange -> {
                val binding = FormItemRearrangeBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    false
                )
                RearrangeItemViewHolder(binding).also {
                    binding.touchIndicator.setOnTouchListener { _, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            startDragListener(it)
                        }
                        false
                    }
                }
            }
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
        updateDragPositions(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: RearrangeItemViewHolder) {
        myViewHolder.itemView.setBackgroundColor(
            ContextCompat.getColor(
                myViewHolder.itemView.context,
                R.color.form_drag_selection
            )
        )
    }

    override fun onRowClear(myViewHolder: RearrangeItemViewHolder) {
        myViewHolder.itemView.setBackgroundColor(
            ContextCompat.getColor(
                myViewHolder.itemView.context,
                android.R.color.transparent
            )
        )

        if (fromInitialPosition != -1 && fromFinalPosition != -1) {
            updateData(fromInitialPosition, fromFinalPosition)
        }
        fromInitialPosition = -1
        fromFinalPosition = -1
    }

    private fun updateData(from: Int, to: Int) {
        val valuePosition = items.indices.toMutableList().apply {
            add(to, removeAt(from))
        }

        val updatedItems = items.mapIndexed { index, item ->
            if (index == valuePosition[index]) return@mapIndexed item

            val updatedValue = items[valuePosition[index]].value
            item.copy(value = updatedValue)
        }

        listener.onValuesChanged(
            updatedItems.map { item ->
                ItemAnswer(
                    questionId = item.inputId,
                    answer = item.value
                )
            }
        )
        items = updatedItems
    }

    private fun updateDragPositions(fromPosition: Int, toPosition: Int) {
        if (fromInitialPosition == -1) {
            fromInitialPosition = fromPosition
        }
        fromFinalPosition = toPosition
    }
}