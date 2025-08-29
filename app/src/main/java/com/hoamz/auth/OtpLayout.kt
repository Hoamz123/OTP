package com.hoamz.auth

import android.R.attr.gravity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getResourceId
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.widget.addTextChangedListener

class OtpLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs,defStyleAttr) {
    private val editTexts = mutableListOf<OtpEditText>()
    private var numberOtp = 6
    private val typedArray = context.obtainStyledAttributes(attrs, R.styleable.OtpLayout)

    init {
        attrs?.let {
            numberOtp = typedArray.getInt(R.styleable.OtpLayout_numberOtp, 6) // <-- đọc giá trị
        }
        createOtpFields()
    }


    fun setNumberOtp(num : Int){
        this.numberOtp = num
    }
    @SuppressLint("ResourceAsColor")
    private fun createOtpFields() {
        editTexts.clear()
        removeAllViews()

        for (i in 0 until numberOtp) {
            val otp = OtpEditText(context).apply {
                id = generateViewId()
                gravity = Gravity.CENTER
                filters = arrayOf(InputFilter.LengthFilter(1))
                setBackgroundColor(Color.LTGRAY)
                val shapeResId = typedArray.getResourceId(R.styleable.OtpLayout_shapeOtp, 0)
                val shape: Drawable? = if (shapeResId != 0) { ContextCompat.getDrawable(context, shapeResId) } else null
                background = shape
            }
            otp.width = dpToPx(45)
            otp.height = dpToPx(45)
            val textColor = typedArray.getColor(R.styleable.OtpLayout_colorText, Color.BLACK)
            otp.setTextColor(textColor)
            // backspace -> focus ô trước
            otp.onBackspaceListener = {
                if (i > 0) editTexts[i - 1].requestFocus()
            }

            // nhập xong -> focus ô sau
            otp.addTextChangedListener {
                if (!it.isNullOrEmpty() && i < numberOtp - 1) {
                    editTexts[i + 1].requestFocus()
                }
            }

            editTexts.add(otp)
            addView(otp)
        }

        val set = ConstraintSet()
        set.clone(this)

        for (i in editTexts.indices) {
            val viewId = editTexts[i].id

            // căn trên/dưới parent → vertical center
            set.connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            set.connect(viewId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

            if (i == 0) {
                set.connect(viewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            } else {
                set.connect(viewId, ConstraintSet.START, editTexts[i - 1].id, ConstraintSet.END)
            }

            if (i == numberOtp - 1) {
                set.connect(viewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            } else {
                set.connect(viewId, ConstraintSet.END, editTexts[i + 1].id, ConstraintSet.START)
            }
        }

        set.applyTo(this)
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

    fun getOtp(): String = editTexts.joinToString("") { it.text.toString() }

}
