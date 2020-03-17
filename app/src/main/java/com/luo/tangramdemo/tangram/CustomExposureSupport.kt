package com.luo.tangramdemo.tangram

import android.util.Log
import android.view.View
import com.tmall.wireless.tangram.dataparser.concrete.Card
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.ExposureSupport

/**
 * 自定义曝光事件
 *
 * @author luojiongtian
 * @since 2019-04-22
 */
class CustomExposureSupport : ExposureSupport() {
    override fun onExposure(card: Card, offset: Int, position: Int) {
        Log.d(TAG, "onExposure: card=" + card.javaClass.simpleName + ", offset=" + offset + ", position=" + position)
    }

    override fun defaultExposureCell(targetView: View, cell: BaseCell<*>, type: Int) {
        Log.d(TAG, "defaultExposureCell: targetView=" + targetView.javaClass.simpleName + ", pos=" + cell.pos + ", type=" + type)
    }

    override fun defaultTrace(targetView: View, cell: BaseCell<*>, type: Int) {
        Log.d(TAG, "defaultTrace: targetView=" + targetView.javaClass.simpleName + ", pos=" + cell.pos + ", type=" + type)
    }

    companion object {
        private const val TAG = "CustomExposureSupport"
    }

    init {
        setOptimizedMode(true)
    }
}