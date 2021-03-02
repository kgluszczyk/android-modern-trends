package com.modern.android.forms.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.modern.android.forms.databinding.FormSummaryFragmentBinding
import com.modern.android.forms.entity.Form
import com.modern.android.forms.ui.FormSpacingItemDecorator
import com.modern.android.forms.ui.showAsNotification
import com.modern.android.presentation.NavigationSummaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

@AndroidEntryPoint
class FormSummaryFragment : Fragment(), SummaryAdapter.SummaryListener {

    private val compositeDisposable = CompositeDisposable()
    private var binding: FormSummaryFragmentBinding? = null
    private var summaryAdapter = SummaryAdapter(this)

    private val navigationSummaryViewModel : NavigationSummaryViewModel by lazy {
        ViewModelProvider(requireActivity()).get(NavigationSummaryViewModel::class.java)
    }

    private val viewModel: FormSummaryViewModel by lazy {
        ViewModelProvider(this).get(FormSummaryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FormSummaryFragmentBinding.inflate(inflater, container, false).apply {
            binding = this
            setupUi()
            updateData(arguments!!.getParcelable(FORM_ARG)!!)
            viewModel.items.subscribe({ summaryAdapter.submitList(it) }) {
                Timber.e(it, "Failed do update items")
            }.addTo(compositeDisposable)
            viewModel.showNotification.subscribe({ if (it) showSavedNotification() }) {
                Timber.e(it, "Failed to show notification")
            }.addTo(compositeDisposable)
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.notification?.clearAnimation()
        binding = null
        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateFragmentVisibility(true)

    }

    override fun onPause() {
        super.onPause()
        viewModel.updateFragmentVisibility(false)
    }

    private fun showSavedNotification() {
        viewModel.notificationShown()
        binding?.notification?.showAsNotification()
    }

    fun updateData(form: Form) {
        viewModel.updateData(form)
    }

    override fun onPageClicked(page: Int) {
        navigationSummaryViewModel.setNavigationAction(NavigationSummaryViewModel.NavigateAction.ScrollToPage(page + 1))
    }

    override fun onSendClicked() {
        navigationSummaryViewModel.setNavigationAction(NavigationSummaryViewModel.NavigateAction.OnSendClick)
    }

    private fun FormSummaryFragmentBinding.setupUi() {
        recycler.apply {
            addItemDecoration(FormSpacingItemDecorator(requireContext()))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = summaryAdapter
        }
    }

    companion object {

        private const val FORM_ARG = "FORM"

        @JvmStatic
        fun newInstance(form: Form) = FormSummaryFragment().apply {
            arguments = createArguments(form)
        }

        @JvmStatic
        fun createArguments(form: Form) = bundleOf(FORM_ARG to form)
    }
}
