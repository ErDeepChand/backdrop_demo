package com.deep.hangout

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView


/**
 * [android.view.View.OnClickListener] used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
class NavigationIconClickListener @JvmOverloads internal constructor(
    private val context: Context,
    private val sheet: View,
    private val interpolator: Interpolator? = null,
    private val openIcon: Int? = null,
    private val closeIcon: Int? = null
) : MenuItem.OnMenuItemClickListener {
    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        backdropShown = !backdropShown

        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()
        updateIcon(menuItem)
        val translateY = height -
                context.resources.getDimensionPixelSize(R.dimen.shr_product_grid_reveal_height)
        val animator = ObjectAnimator.ofFloat(sheet, "translationY", (if (backdropShown) translateY else 0).toFloat())

        animator.duration = 500
        if (interpolator != null) {
            animator.interpolator = interpolator
        }
        animatorSet.play(animator)
        animator.start()
        return true
    }

    private fun updateIcon(view: MenuItem?) {
        if (openIcon != null && closeIcon != null) {
            require(view is MenuItem) { "updateIcon() must be called on an MenuItem" }
            if (backdropShown) {
                view.setIcon(closeIcon)
            } else {
                view.setIcon(openIcon)
            }
        }
    }
}