package com.modern.android.forms.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.modern.android.forms.databinding.FormMainFragmentBinding
import com.modern.android.forms.di.PageViewModelFactory
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.ui.FormSpacingItemDecorator
import com.modern.android.forms.ui.ResettableFragment
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class FormPageFragment : DaggerFragment(), OnValueChangedListener, ResettableFragment {

    @Inject
    lateinit var viewModelFactory: PageViewModelFactory

    private val viewModel: PageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PageViewModel::class.java)
    }

    private val disposables = CompositeDisposable()
    private var binding: FormMainFragmentBinding? = null
    private var formAdapter = FormAdapter(this@FormPageFragment)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FormMainFragmentBinding.inflate(inflater, container, false).apply {
            binding = this
            setupUi()
            viewModel.items.subscribe({ formAdapter.items = it }) {
                Timber.e(it, "Error while refreshing page items")
            }.addTo(disposables)
            updateData(arguments!!.getParcelable(PAGE_ARG)!!)
        }.root
    }

    private fun FormMainFragmentBinding.setupUi() {
        recycler.apply {
            addItemDecoration(FormSpacingItemDecorator(requireContext()))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = formAdapter
        }
    }

    fun updateData(page: com.modern.android.forms.entity.Page) {
        viewModel.updateData(page)
    }

    override fun reset() {
        binding?.recycler?.scrollToPosition(0)
    }

    override fun onValuesChanged(answers: List<ItemAnswer>) {
        viewModel.updateAnswer(answers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    companion object {

        private const val PAGE_ARG = "PAGE"

        @JvmStatic
        fun newInstance(page: com.modern.android.forms.entity.Page) = FormPageFragment().apply {
            arguments = createArguments(page)
        }

        @JvmStatic
        fun createArguments(page: com.modern.android.forms.entity.Page) = bundleOf(PAGE_ARG to page)
    }
}