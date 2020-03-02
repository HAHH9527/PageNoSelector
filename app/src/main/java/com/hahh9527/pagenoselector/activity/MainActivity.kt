package com.hahh9527.pagenoselector.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hahh9527.pagenoselector.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPageNoSelector()
    }

    private fun initPageNoSelector() {
        pns.pageChangeCallBack = { pageNo ->
            Toast.makeText(applicationContext, "翻页至:$pageNo", Toast.LENGTH_SHORT).show()
            text_page_no.text = "目前页码:$pageNo"
        }
    }
}
