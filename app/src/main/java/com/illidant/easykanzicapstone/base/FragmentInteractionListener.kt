package com.illidant.easykanzicapstone.base

interface FragmentInteractionListener {
    fun setToolbarTitle(title: String)

    fun setToolbarVisibility(show: Boolean)

    fun setCurrentFragment(fragment: BaseFragment)

    fun scroll(input: Int)
}
