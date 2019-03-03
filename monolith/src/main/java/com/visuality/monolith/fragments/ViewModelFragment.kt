package com.visuality.monolith.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStoreOwner
import java.lang.reflect.ParameterizedType

abstract class ViewModelFragment<FragmentViewModel : ViewModel> : BaseFragment() {

    lateinit var viewModel: FragmentViewModel
        private set

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.prepareViewModel()
        this.onViewModelCreated()
    }

    protected abstract fun getViewModelOwner(): ViewModelStoreOwner

    protected abstract fun onViewModelCreated()

    private fun getViewModelClass(): Class<FragmentViewModel> {
        val parameterizedType = this.javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0] as Class<FragmentViewModel>
    }

    private fun prepareViewModel() {
        val owner = this.getViewModelOwner()

        val provider = when (owner) {
            is FragmentActivity -> ViewModelProviders.of(owner)
            is Fragment -> ViewModelProviders.of(owner)
            else -> null
        } ?: return

        this.viewModel = provider.get(
            this.getViewModelClass()
        )
    }
}
