package com.luo.tangramdemo.tangram

import android.view.View
import android.widget.Toast
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.SimpleClickSupport

/**
 * 自定义点击事件
 *
 * @author luojiongtian
 * @since 2019-04-22
 */
class CustomClickSupport : SimpleClickSupport() {
    override fun defaultClick(targetView: View, cell: BaseCell<*>, eventType: Int) {
        Toast.makeText(targetView.context,
                "您点击了组件，type=" + cell.stringType + ", pos=" + cell.pos, Toast.LENGTH_SHORT).show()
    }

    init {
        setOptimizedMode(true)
    }
}