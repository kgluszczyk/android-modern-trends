package com.modern.android.forms.ui.page

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.modern.android.forms.entity.FormItem
import com.modern.android.forms.ui.ResettableFragment
import com.modern.android.forms.ui.summary.FormSummaryFragment
import java.lang.ref.WeakReference

class PageFormAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var data: List<FormItem> = emptyList()
    private val fragments = SparseArray<WeakReference<Fragment>>()
    private var previousFragmentPosition = -1

    override fun getItem(position: Int): Fragment {
        val item = data[position]
        return when (item) {
            is FormItem.PageItem -> FormPageFragment.newInstance(item.page)
            is FormItem.SummaryItem -> FormSummaryFragment.newInstance(item.form)
        }.also { fragments.put(position, WeakReference(it)) }
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        fragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getCount() = data.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position == previousFragmentPosition) return
        (getPage(previousFragmentPosition) as? ResettableFragment)?.reset()
        previousFragmentPosition = position
    }

    private fun getPage(position: Int) = fragments[position]?.get()

    fun setData(data: List<FormItem>) {
        if (this.data.size != data.size) {
            this.data = data
            notifyDataSetChanged()
        } else {
            this.data = data
            updateFragmentData()
        }
    }

    private fun updateFragmentData() {
        data.indices.forEach { position ->
            val page = getPage(position)
            val item = data[position]
            when {
                page is FormPageFragment && item is FormItem.PageItem -> page.updateData(item.page)
                page is FormSummaryFragment && item is FormItem.SummaryItem -> page.updateData(item.form)
            }
        }
    }
}