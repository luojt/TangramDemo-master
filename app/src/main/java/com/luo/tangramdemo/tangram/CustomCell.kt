package com.luo.tangramdemo.tangram

import com.tmall.wireless.tangram.MVHelper
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.ExposureSupport
import org.json.JSONException
import org.json.JSONObject

/**
 * 自定义model
 *
 * @author luojiongtian
 * @since 2020-03-17
 */
class CustomCell : BaseCell<CustomCellView>() {
    private var imageUrl: String? = null
    private var text: String? = null
    override fun parseWith(data: JSONObject, resolver: MVHelper) {
        try {
            if (data.has("imageUrl")) {
                imageUrl = data.getString("imageUrl")
            }
            if (data.has("text")) {
                text = data.getString("text")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun bindView(view: CustomCellView) {
        if (pos % 2 == 0) {
            view.setBackgroundColor(-0xff01)
        } else {
            view.setBackgroundColor(-0x100)
        }
        view.setImageUrl(imageUrl)
        view.setText(view.javaClass.simpleName + pos + ": " + text)
        view.setOnClickListener(this)
        if (serviceManager != null) {
            val exposureSupport = serviceManager!!.getService(ExposureSupport::class.java)
            exposureSupport?.onTrace(view, this, type)
        }
    }
}