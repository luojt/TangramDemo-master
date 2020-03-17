package com.luo.tangramdemo.virtualview

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.luo.tangramdemo.R
import com.luo.tangramdemo.Utils.getAssetsFile
import com.luo.tangramdemo.Utils.getJSONDataFromAsset
import com.luo.tangramdemo.Utils.isValidContextForGlide
import com.luo.tangramdemo.virtualview.VirtualViewActivity
import com.tmall.wireless.vaf.framework.VafContext
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader.IImageLoaderAdapter
import com.tmall.wireless.vaf.virtualview.core.IContainer
import com.tmall.wireless.vaf.virtualview.event.EventManager
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase

class VirtualViewActivity : AppCompatActivity() {
    private var mLinearLayout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virtual_view)
        mLinearLayout = findViewById(R.id.layout)
        initVirtualView()
    }

    private fun initVirtualView() {
        val vafContext = VafContext(applicationContext)
        vafContext.setImageLoaderAdapter(object : IImageLoaderAdapter {
            override fun bindImage(uri: String, imageBase: ImageBase, reqWidth: Int, reqHeight: Int) {
                if (isValidContextForGlide(this@VirtualViewActivity)) {
                    val requestBuilder: RequestBuilder<*> = Glide.with(this@VirtualViewActivity).asBitmap().load(uri)
                    if (reqWidth > 0 || reqHeight > 0) {
                        requestBuilder.submit(reqWidth, reqHeight)
                    }
                    val imageTarget = ImageTarget(imageBase)
                    requestBuilder.into(imageTarget)
                }
            }

            override fun getBitmap(uri: String, reqWidth: Int, reqHeight: Int, lis: ImageLoader.Listener) {
                if (isValidContextForGlide(this@VirtualViewActivity)) {
                    val requestBuilder: RequestBuilder<*> = Glide.with(this@VirtualViewActivity).asBitmap().load(uri)
                    if (reqWidth > 0 || reqHeight > 0) {
                        requestBuilder.submit(reqWidth, reqHeight)
                    }
                    val imageTarget = ImageTarget(lis)
                    requestBuilder.into(imageTarget)
                }
            }
        })
        val viewManager = vafContext.viewManager
        viewManager.init(applicationContext)
        //        viewManager.loadBinBufferSync(VVTEST.BIN);
//        viewManager.loadBinFileSync("file:///android_asset/VVTest.out");
        viewManager.loadBinBufferSync(getAssetsFile(this, "VVTest.out"))
        viewManager.loadBinBufferSync(getAssetsFile(this, "ViewPager.out"))
        vafContext.eventManager.register(EventManager.TYPE_Click) { data ->
            Toast.makeText(this@VirtualViewActivity, data.mVB.action, Toast.LENGTH_SHORT).show()
            true
        }
        vafContext.eventManager.register(EventManager.TYPE_Exposure) { data ->
            Log.d(TAG, "Exposure process: " + data.mVB.viewCache.componentData)
            true
        }
        // VVTest
        var container = vafContext.containerService.getContainer("VVTest", true)
        mLinearLayout!!.addView(container)
        var iContainer = container as IContainer
        var jsonObject = getJSONDataFromAsset(this, "vvtest.json")
        if (jsonObject != null) {
            iContainer.virtualView.setVData(jsonObject)
        }
        // ViewPager
        container = vafContext.containerService.getContainer("ViewPager", true)
        mLinearLayout!!.addView(container)
        iContainer = container as IContainer
        jsonObject = getJSONDataFromAsset(this, "view_pager.json")
        if (jsonObject != null) {
            iContainer.virtualView.setVData(jsonObject)
        }
    }

    companion object {
        private const val TAG = "VirtualViewActivity"
    }

    private fun Any.into(imageTarget: ImageTarget) {

    }
}


