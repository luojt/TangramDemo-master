package com.luo.tangramdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.luo.tangramdemo.tangram.TangramActivity
import com.luo.tangramdemo.virtualview.VirtualViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv_tangram).setOnClickListener { startActivity(Intent(this@MainActivity, TangramActivity::class.java)) }
        findViewById<View>(R.id.tv_virtual_view).setOnClickListener { startActivity(Intent(this@MainActivity, VirtualViewActivity::class.java)) }
    }
}