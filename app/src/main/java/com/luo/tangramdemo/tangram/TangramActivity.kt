package com.luo.tangramdemo.tangram

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.luo.tangramdemo.R
import com.luo.tangramdemo.Utils.getAssetsFile
import com.luo.tangramdemo.Utils.isValidContextForGlide
import com.luo.tangramdemo.tangram.CustomAnnotationView
import com.luo.tangramdemo.tangram.CustomCellView
import com.luo.tangramdemo.tangram.CustomInterfaceView
import com.luo.tangramdemo.tangram.NoBackgroundView
import com.luo.tangramdemo.tangram.TangramActivity
import com.luo.tangramdemo.virtualview.ImageTarget
import com.tmall.wireless.tangram.TangramBuilder
import com.tmall.wireless.tangram.TangramEngine
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator
import com.tmall.wireless.tangram.support.async.AsyncLoader
import com.tmall.wireless.tangram.support.async.AsyncPageLoader
import com.tmall.wireless.tangram.support.async.CardLoadSupport
import com.tmall.wireless.tangram.util.IInnerImageSetter
import com.tmall.wireless.vaf.framework.VafContext
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader
import com.tmall.wireless.vaf.virtualview.Helper.ImageLoader.IImageLoaderAdapter
import com.tmall.wireless.vaf.virtualview.event.EventManager
import com.tmall.wireless.vaf.virtualview.view.image.ImageBase
import org.json.JSONArray
import org.json.JSONException

class TangramActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mEngine: TangramEngine? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tangram)
        mRecyclerView = findViewById(R.id.recycler_view)
        initTangram()
    }

    private fun initTangram() { // 初始化 Tangram 环境
        TangramBuilder.init(this, object : IInnerImageSetter {
            override fun <IMAGE : ImageView?> doLoadImageUrl(view: IMAGE, url: String?) {
                Glide.with(this@TangramActivity).load(url).into(view)
            }
        }, ImageView::class.java)
        // 初始化 TangramBuilder
        val builder = TangramBuilder.newInnerBuilder(this)
        // 注册自定义的卡片和组件
        builder.registerCell("InterfaceCell", CustomInterfaceView::class.java)
        builder.registerCell("AnnotationCell", CustomAnnotationView::class.java)
        builder.registerCell("CustomCell", CustomCell::class.java, CustomCellView::class.java)
        builder.registerCell<View>("HolderCell", CustomHolderCell::class.java, ViewHolderCreator(R.layout.item_holder, CustomViewHolder::class.java, TextView::class.java))
        builder.registerCell("NoBackground", NoBackgroundView::class.java)
        // 注册 VirtualView 版本的 Tangram 组件
        builder.registerVirtualView<View>("VVTest")
        // 生成TangramEngine实例
        mEngine = builder.build()
        // 加载VirtualView模板数据
        //        mEngine.setVirtualViewTemplate(VVTEST.BIN);
        mEngine!!.setVirtualViewTemplate(getAssetsFile(this, "VVTest.out"))
        // 绑定业务 support 类到 engine
        // 处理点击
        mEngine!!.addSimpleClickSupport(CustomClickSupport())
        // 处理曝光
        mEngine!!.addExposureSupport(CustomExposureSupport())
        // 异步加载数据
        val cardLoadSupport = CardLoadSupport(AsyncLoader { card, callback -> Log.d(TAG, "loadData: cardType=" + card.stringType) }, AsyncPageLoader { page, card, callback -> Log.d(TAG, "loadData: page=" + page + ", cardType=" + card.stringType) })
        CardLoadSupport.setInitialPage(1)
        mEngine!!.addCardLoadSupport(cardLoadSupport)
        val vafContext = mEngine!!.getService(VafContext::class.java)
        vafContext.setImageLoaderAdapter(object : IImageLoaderAdapter {
            override fun bindImage(uri: String, imageBase: ImageBase, reqWidth: Int, reqHeight: Int) {
                if (isValidContextForGlide(this@TangramActivity)) {
                    val requestBuilder: RequestBuilder<*> = Glide.with(this@TangramActivity).asBitmap().load(uri)
                    if (reqWidth > 0 || reqHeight > 0) {
                        requestBuilder.submit(reqWidth, reqHeight)
                    }
                    val imageTarget = ImageTarget(imageBase)
                    requestBuilder.into(imageTarget)
                }
            }

            override fun getBitmap(uri: String, reqWidth: Int, reqHeight: Int,
                                   lis: ImageLoader.Listener) {
                if (isValidContextForGlide(this@TangramActivity)) {
                    val requestBuilder: RequestBuilder<*> = Glide.with(this@TangramActivity).asBitmap().load(uri)
                    if (reqWidth > 0 || reqHeight > 0) {
                        requestBuilder.submit(reqWidth, reqHeight)
                    }
                    val imageTarget = ImageTarget(lis)
                    requestBuilder.into(imageTarget)
                }
            }
        })
        // 注册VirtualView事件处理器
        vafContext.eventManager.register(EventManager.TYPE_Click) { data ->
            Toast.makeText(this@TangramActivity, data.mVB.action, Toast.LENGTH_SHORT).show()
            true
        }
        vafContext.eventManager.register(EventManager.TYPE_Exposure) { data ->
            Log.d(TAG, "Exposure process: " + data.mVB.viewCache.componentData)
            true
        }
        // 绑定 recyclerView
        mEngine!!.bindView(mRecyclerView!!)
        // 监听 recyclerView 的滚动事件
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                mEngine!!.onScrolled()
            }
        })
        // 设置悬浮类型布局的偏移（可选）
        mEngine!!.layoutManager.setFixOffset(0, 40, 0, 0)
        // 设置卡片预加载的偏移量（可选）
        mEngine!!.setPreLoadNumber(3)
        // 加载数据并传递给 engine
        val bytes = getAssetsFile(this, "data.json")
        if (bytes != null) {
            val json = String(bytes)
            try {
                val data = JSONArray(json)
                mEngine!!.setData(data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() { // 退出的时候销毁 engine
        mEngine!!.destroy()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TangramActivity"
    }

    private fun Any.into(imageTarget: ImageTarget) {

    }
}

private fun <IMAGE> Any.into(view: IMAGE) {

}
