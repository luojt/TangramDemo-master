package com.luo.tangramdemo.tangram

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.luo.tangramdemo.Utils
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle
import com.tmall.wireless.tangram.support.ExposureSupport
import java.util.*

/**
 * 没有设置背景的自定义View
 *
 * @author luojiongtian
 * @since 2020-03-17
 */
class NoBackgroundView : LinearLayout, ITangramViewLifeCycle {
    private var mTextView: TextView? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        val padding = Utils.dip2px(context, 10f)
        setPadding(padding, padding, padding, padding)
        mTextView = TextView(context)
        mTextView!!.textSize = 12f
        addView(mTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun cellInited(cell: BaseCell<*>) {
        setOnClickListener(cell)
        if (cell.serviceManager != null) {
            val exposureSupport = cell.serviceManager!!.getService(ExposureSupport::class.java)
            exposureSupport?.onTrace(this, cell, cell.type)
        }
    }

    override fun postBindView(cell: BaseCell<*>) {
        mTextView!!.text = String.format(Locale.CHINA, "%s%d: %s", javaClass.simpleName,
                cell.pos, cell.optParam("text"))
    }

    override fun postUnBindView(cell: BaseCell<*>?) {}
}