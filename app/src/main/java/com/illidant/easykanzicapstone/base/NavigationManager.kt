package com.illidant.easykanzicapstone.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.illidant.easykanzicapstone.R


class NavigationManager(
    private val fragmentManager: FragmentManager,
    private val container: Int
) {
    val isRootFragmentVisible: Boolean =
        fragmentManager.backStackEntryCount <= STACK_ENTRY_COUNT_LIMIT

    private var navigationListener: (() -> Unit)? = null

    init {
        fragmentManager.addOnBackStackChangedListener {
            navigationListener?.invoke()
        }
    }

    fun open(fragment: Fragment) {
        openFragment(fragment, true, false)
    }

    fun openAsRoot(fragment: Fragment) {
        popEveryFragment()
        openFragment(fragment, false, true)
    }

    fun navigateBack(): Boolean = if (fragmentManager.backStackEntryCount == 0) {
        false
    } else {
        fragmentManager.popBackStackImmediate()
        true
    }


    private fun openFragment(fragment: Fragment, addToBackStack: Boolean, isRoot: Boolean) {
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (isRoot) {
            fragmentTransaction.replace(container, fragment, FRAGMENT_TAG_ROOT)
        } else {
            fragmentTransaction.replace(container, fragment)
        }

        fragmentTransaction.setAnimations()

        if (addToBackStack) fragmentTransaction.addToBackStack(fragment.toString())
        
        fragmentTransaction.commit()
    }

    private fun FragmentTransaction.setAnimations() =
        setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_out_right,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )

    private fun popEveryFragment() {
        fragmentManager.popBackStackImmediate(
            FRAGMENT_TAG_ROOT,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val STACK_ENTRY_COUNT_LIMIT = 1
        private const val FRAGMENT_TAG_ROOT = "ROOT"
    }
}
