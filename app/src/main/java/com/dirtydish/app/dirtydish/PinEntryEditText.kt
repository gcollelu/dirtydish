package com.dirtydish.app.dirtydish

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.graphics.Paint
import android.view.View
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.widget.AppCompatEditText
import android.util.TypedValue
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.dirtydish.app.dirtydish.R.attr.*


class PinEntryEditText : AppCompatEditText {
    private var mSpace = 24f //24 dp by default, space between the lines
    private var mCharSize: Float = 0.toFloat()
    private var mNumChars = 4f
    private var mLineSpacing = 8f //8dp by default, height of the text from our lines
    private var mMaxLength = 4f
    val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"

    var mClickListener: View.OnClickListener? = null

    private var mLineStroke = 1f //1dp by default
    private var mLineStrokeSelected = 2f //2dp by default
    private var mLinesPaint: Paint? = null


    private var mStates = arrayOf(
            intArrayOf(android.R.attr.state_selected), // selected
            intArrayOf(android.R.attr.state_focused), // focused
            intArrayOf(-android.R.attr.state_focused))// unfocused

    private var mColors = intArrayOf(Color.GREEN, Color.MAGENTA, Color.WHITE)

    private var mColorStates = ColorStateList(mStates, mColors)


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        paint.color = Color.WHITE
        val multi = context.resources.displayMetrics.density
        mLineStroke = (multi * mLineStroke)
        mLineStrokeSelected = (multi * mLineStrokeSelected)
        mLinesPaint = Paint(paint)
        mLinesPaint?.strokeWidth = mLineStroke
        if (!isInEditMode) {
            val outValue = TypedValue()
            context.theme.resolveAttribute(colorControlActivated,
                    outValue, true)
            val colorActivated = outValue.data
            mColors[0] = colorActivated

            context.theme.resolveAttribute(colorPrimaryDark,
                    outValue, true)
            val colorDark = outValue.data
            mColors[1] = colorDark

            context.theme.resolveAttribute(colorControlHighlight,
                    outValue, true)
            val colorHighlight = outValue.data
            mColors[2] = colorHighlight
        }
        setBackgroundResource(0)
        mSpace = (multi * mSpace) //convert to pixels for our density
        mLineSpacing = (multi * mLineSpacing) //convert to pixels for our density
        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4).toFloat()
        mNumChars = mMaxLength

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        })
        // When tapped, move cursor to end of text.
        super.setOnClickListener { v ->
            setSelection(text!!.length)
            if (mClickListener != null) {
                mClickListener?.onClick(v)
            }
        }

    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        val availableWidth = width - paddingRight - paddingLeft
        if (mSpace < 0) {
            mCharSize = availableWidth / (mNumChars * 2 - 1)
        } else {
            mCharSize = (availableWidth - mSpace * (mNumChars - 1)) / mNumChars
        }

        var startX = paddingLeft.toFloat()
        val bottom = height - paddingBottom

        //Text width
        val text = text
        val textLength = text!!.length
        val textWidths = FloatArray(textLength)
        paint.getTextWidths(getText(), 0, textLength, textWidths)


        for (i in 0..mNumChars.toInt()) {
            updateColorForLines(i == textLength)
            canvas.drawLine(startX, bottom.toFloat(), startX + mCharSize, bottom.toFloat(), paint)
            if (text.length > i) {
                val middle = startX + mCharSize / 2
                canvas.drawText(text, i,i + 1, middle - textWidths[0] / 2, (bottom - mLineSpacing), paint)
            }

            if (mSpace < 0) {
                startX += mCharSize * 2
            } else {
                startX += mCharSize + mSpace
            }
        }
    }

    private fun getColorForState(vararg states: Int): Int {
        return mColorStates.getColorForState(states, Color.WHITE)
    }

    private fun updateColorForLines(next: Boolean) {
        if (isFocused) {
            mLinesPaint?.strokeWidth = mLineStrokeSelected
            mLinesPaint?.color = getColorForState(android.R.attr.state_focused)
            if (next) {
                mLinesPaint?.color = getColorForState(android.R.attr.state_selected)
            }
        } else {
            mLinesPaint?.strokeWidth = mLineStroke
            mLinesPaint?.color = getColorForState(-android.R.attr.state_focused)
        }
    }
}