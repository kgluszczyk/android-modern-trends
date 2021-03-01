package com.modern.android.forms

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.modern.android.forms.databinding.FormFragmentFormRootBinding
import com.modern.android.forms.di.FormRendererViewModelFactory
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.entity.FormItem
import com.modern.android.forms.presentation.FormRendererViewModel
import com.modern.android.forms.ui.SummaryCallback
import com.modern.android.forms.ui.hideNotification
import com.modern.android.forms.ui.page.PageFormAdapter
import com.modern.android.forms.ui.send.SendAnswersDialogFragment
import com.modern.android.forms.ui.showAsNotification
import com.modern.android.forms.ui.showError
import com.modern.commons.utils.Resource
import com.modern.commons.utils.ResourceError
import com.modern.commons.utils.ResourceLoading
import com.modern.commons.utils.ResourceSuccess
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class FormRendererFragment : DaggerFragment(), ViewPager.OnPageChangeListener, SummaryCallback {

    private val PAGE_OFFSET = 2

    val formContext: FormContext by lazy { requireNotNull(arguments).getParcelable(FORM_CONTEXT_ARG)!! }

    @Inject
    lateinit var viewModelFactory: FormRendererViewModelFactory
    private val viewModel: FormRendererViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(FormRendererViewModel::class.java)
    }

    private val compositeDisposable = CompositeDisposable()
    private val adapter by lazy { PageFormAdapter(childFragmentManager) }
    private var binding: FormFragmentFormRootBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FormFragmentFormRootBinding.inflate(inflater).apply {
            pager.offscreenPageLimit = PAGE_OFFSET
            pager.adapter = adapter
            pager.addOnPageChangeListener(this@FormRendererFragment)
            notificationClose.setOnClickListener { notification.hideNotification() }
            home.setOnClickListener {
                if (isSummaryPage()) {
                    closeForm()
                } else {
                    goToHomePage()
                }
            }
            back.setOnClickListener {
                pager.setCurrentItem(max(0, pager.currentItem - 1), true)
            }
            next.setOnClickListener {
                if (pager.currentItem + 1 >= adapter.count) {
                    pager.setCurrentItem(0, true)
                } else {
                    pager.setCurrentItem(min(adapter.count - 1, pager.currentItem + 1), true)
                }

            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemsRx.subscribe({ it.updateResource() }, {
            Timber.e(it, "Failed to load items")
        }).addTo(compositeDisposable)

        viewModel.itemsLiveData.observe(viewLifecycleOwner, {
            it.updateResource()
        })

        lifecycleScope.launchWhenStarted {
            viewModel.itemsStateFlow.collect { form ->
                form.updateResource()
            }
        }
    }

    private fun Resource<List<FormItem>, Throwable>.updateResource() {
        binding?.loading?.isVisible = this is ResourceLoading
        when (this) {
            is ResourceSuccess -> adapter.setData(data)
            is ResourceError -> handleLoadError(error)
        }
    }

    private fun handleLoadError(error: Throwable) {
        when {
            else -> requireContext().showError(
                R.string.form_oops,
                R.string.form_error_message,
                requireActivity()::finish
            ) {
                viewModel.loadData()
            }
        }
    }

    override fun scrollToPage(page: Int) {
        binding?.pager?.setCurrentItem(page, true)
    }

    override fun onSendClick() {
        if (!viewModel.validateAnswers()) {
            binding?.notification?.showAsNotification()
            return
        }
        SendAnswersDialogFragment.create(viewModel.createFormResults())
            .show(childFragmentManager, "answers dialog")
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewModel.saveAnswers()
        binding?.apply {
            label.text = getString(R.string.form_of, position, adapter.count - 1)
            next.text = getString(if (position + 1 == adapter.count) R.string.form_finish else R.string.form_next)
            navigation.isVisible = position != 0
        }

        binding?.root?.hideKeyboard()
    }

    private fun View.hideKeyboard() {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        binding?.notification?.clearAnimation()
        binding = null
        compositeDisposable.clear()
        super.onDestroyView()
    }

    fun onBackPressed() {
        if (isSummaryPage()) {
            closeForm()
        } else {
            binding?.pager?.let {
                it.setCurrentItem(it.currentItem - 1, true)
            }
        }
    }

    private fun goToHomePage() {
        binding?.pager?.setCurrentItem(0, true)
    }

    private fun closeForm() {
        activity?.finish()
    }

    private fun isSummaryPage() = binding?.pager?.currentItem == 0

    companion object {

        private const val FORM_CONTEXT_ARG = "FORM_CONTEXT_ARG"

        @JvmStatic
        fun createArguments(formContext: FormContext) = bundleOf(FORM_CONTEXT_ARG to formContext)
    }
}

@BindingAdapter("layout_width")
fun setLayoutHeight(view: View, width: Int) {
    val layoutParams = view.layoutParams
    layoutParams.width = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        width.toFloat(),
        view.resources.displayMetrics
    ).toInt()
    view.layoutParams = layoutParams
}