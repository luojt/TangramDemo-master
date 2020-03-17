package com.luo.tangramdemo.tangram

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.luo.tangramdemo.R
import com.luo.tangramdemo.Utils
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle
import com.tmall.wireless.tangram.support.ExposureSupport
import java.util.*

/**
 * 使用接口方式的自定义View
 *
 * @author luojiongtian
 * @since 2020-03-17
 */
class CustomInterfaceView : LinearLayout, ITangramViewLifeCycle {
    private var mImageView: ImageView? = null
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
        mImageView = ImageView(context)
        addView(mImageView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mTextView = TextView(context)
        mTextView!!.setPadding(0, padding, 0, 0)
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
        if (cell.pos % 2 == 0) {
            setBackgroundColor(-0x10000)
            mImageView!!.setImageResource(R.mipmap.ic_launcher)
        } else {
            setBackgroundColor(-0xff0100)
            mImageView!!.setImageResource(R.mipmap.ic_launcher_round)
        }
        mTextView!!.text = String.format(Locale.CHINA, "%s%d: %s", javaClass.simpleName,
                cell.pos, cell.optParam("text"))
    }

    override fun postUnBindView(cell: BaseCell<*>?) {}
}