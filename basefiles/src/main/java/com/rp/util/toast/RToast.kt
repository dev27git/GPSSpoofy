package com.rp.util.toast

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.rp.basefiles.R

const val DEFAULT = 0
const val WARNING = 1
const val ERROR = 2

const val DEFAULT_COLOR = Color.GREEN
const val WARNING_COLOR = Color.YELLOW
const val ERROR_COLOR = Color.RED

class RToast(context: Context?) : Toast(context) {

    @IntDef(DEFAULT, WARNING, ERROR)
    @Retention(AnnotationRetention.BINARY)
    annotation class Type

    private var message: String? = null
    private var messageType: Int = DEFAULT

    fun showToast() {
        view?.run {
            findViewById<AppCompatTextView>(R.id.tvMessage).text = message
            findViewById<LinearLayout>(R.id.llBackground).setBackgroundColor(
                    when(messageType) {
                        ERROR -> ERROR_COLOR
                        WARNING -> WARNING_COLOR
                        else -> DEFAULT_COLOR
                    }
            )
        }
        show()
    }

    companion object Builder {

        private var rToast: RToast? = null

        fun init(context: Context): Builder {
            rToast = RToast(context)
            rToast?.view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
            return this
        }

        fun text(@NonNull message: String) : Builder {
            rToast?.message = message
            return this
        }

        fun text(@Type @StringRes messageRes: Int) : Builder = text(rToast?.view?.context?.getString(messageRes) ?: "")

        fun type(@Type type: Int) : Builder {
            rToast?.messageType = type
            return this
        }

        fun show() = rToast?.showToast()

        fun cancel() = rToast?.cancel()

    }
}