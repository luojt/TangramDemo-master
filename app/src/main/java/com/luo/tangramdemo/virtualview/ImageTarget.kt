package com.luo.tangramdemo.virtualview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase

/**
 * 封装的Glide Target
 *
 * @author luojiongtian
 * @since 2019-04-24
 */
class ImageTarget : SimpleTarget<Bitmap?> {
    var mImageBase: ImageBase? = null
    var mListener: ImageLoader.Listener? = null

    constructor(imageBase: ImageBase?) {
        mImageBase = imageBase
    }

    constructor(listener: ImageLoader.Listener?) {
        mListener = listener
    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
        mImageBase!!.setBitmap(resource, true)
        if (mListener != null) {
            mListener!!.onImageLoadSuccess(resource)
        }
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        if (mListener != null) {
            mListener!!.onImageLoadFailed()
        }
    }
}