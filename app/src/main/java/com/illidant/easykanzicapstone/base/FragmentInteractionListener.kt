package com.sun.basic_japanese.base

interface FragmentInteractionListener {
    fun setToolbarTitle(title: String)

    fun setToolbarVisibility(show: Boolean)

    fun setCurrentFragment(fragment: BaseFragment)

    fun scroll(input: Int)
}
