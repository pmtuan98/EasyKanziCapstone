package com.illidant.easykanzicapstone.base

import android.app.Activity
import android.app.Fragment
import android.content.Context


open class BaseFragment : Fragment() {

    private lateinit var navigationManagerInner: NavigationManager
    private lateinit var fragmentInteractionInner: FragmentInteractionListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        navigationManagerInner =
            if (parentFragment != null && parentFragment is HasNavigationManager) {
                (parentFragment as HasNavigationManager).provideNavigationManager()
            } else if (context is HasNavigationManager) {
                (context as HasNavigationManager).provideNavigationManager()
            } else {
                throw RuntimeException(ERROR_IMPLEMENT_HAS_NAVIGATION_MANAGER)
            }

        if (context is Activity) {
            fragmentInteractionInner = context as FragmentInteractionListener
        } else {
            throw RuntimeException(ERROR_IMPLEMENT_FRAGMENT_INTERACTION_LISTENER)
        }
    }

    override fun onStart() {
        super.onStart()
        if (::fragmentInteractionInner.isInitialized) {
            fragmentInteractionInner.setCurrentFragment(this)
        }
    }

    fun getNavigationManager(): NavigationManager = navigationManagerInner

    open fun onBackPressed() = false

    companion object {
        private const val ERROR_IMPLEMENT_HAS_NAVIGATION_MANAGER =
            "Activity host must implement HasNavigationManager"
        private const val ERROR_IMPLEMENT_FRAGMENT_INTERACTION_LISTENER =
            "Activity host must implement FragmentInteractionListener"
    }
}
