package edu.auburn.sips_android_official.ui.adapters

import android.databinding.BindingAdapter
import android.view.View

/**
 * Created by clj00 on 2/19/2018.
 */
class BindingAdapters {

    companion object {
        @JvmStatic
        @BindingAdapter("visibleGone")
        fun showHide(view: View, show: Boolean) {
            view.setVisibility(if (show) View.VISIBLE else View.GONE)
        }
    }

}