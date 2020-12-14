package com.chuanliu.dest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    val tAG = "TestMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val haulView = findViewById<HaulView>(R.id.haulView)
        val linearLayout = LinearLayoutManager(this)

//        linearLayout.orientation = LinearLayoutManager.HORIZONTAL
        var list = ArrayList<String>()
        list.add("first")
        list.add("four\new8\new9\nnew10\nnew11")
        list.add("second\nNew2")
        list.add("third\nnew3\nnew4")
        list.add("four\new5\new6")
        list.add("five")
        list.add("six")
        list.add("third\nnew3\nnew4")
        list.add("seven")
        list.add("four\new5\new6")
        list.add("eight")
        list.add("nine")
        list.add("ten")
        list.add("first")
        list.add("four\new5\new6")
        list.add("second")
        list.add("third")
        list.add("four")
        list.add("four\new8\new9\nnew10\nnew11")
        list.add("five")
        list.add("six")
        list.add("seven")
        list.add("eight")
        list.add("nine")
        list.add("four\new5\new6")
        list.add("ten")
        list.add("first")
        list.add("second")
        list.add("third")
        list.add("four\new5\new6")
        list.add("four")
        list.add("five")
        list.add("six")
        list.add("seven")
        list.add("eight")
        list.add("nine")
        list.add("ten")
        list.add("first")
        list.add("second")
        list.add("third")
        list.add("four")
        list.add("five")
        list.add("six")
        list.add("seven")
        list.add("eight")
//        list.add("nine")
//        list.add("ten")
//        list.add("first")
//        list.add("second")
//        list.add("third")
//        list.add("four")
//        list.add("five")
//        list.add("six")
//        list.add("seven")
//        list.add("eight")
//        list.add("nine")
//        list.add("ten")
//        list.add("first")
//        list.add("second")
//        list.add("third")
//        list.add("four")
//        list.add("five")
//        list.add("six")
//        list.add("seven")
//        list.add("eight")
//        list.add("nine")
//
//        list.add("ten")
//        list.add("four\new12\new13\nnew14\nnew15\nnew16\nnew17\nnew18\nnew19")
        Log.e(tAG, "lisSize:${list.size}")
        var adapter = MyAdapter(this, list)
        haulView.recyView.layoutManager = linearLayout
//        haulView.recyView.layoutManager = GridLayoutManager(this, 2)
//        haulView.recyView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        haulView.recyView.adapter = adapter

        haulView.resultCall = object : HaulView.ResultCall {
            override fun onFresh() {
                Log.e(tAG, "activity onFresh")
                haulView.postDelayed({
                    haulView.freshSuc()
                    list.add(0,"200")
                    list.add(0,"100")
                    (haulView.recyView.adapter as MyAdapter).notifyDataSetChanged()
                },3000)
            }

            override fun onMore() {
                Log.e(tAG, "activity onMore")
                haulView.postDelayed({
//                    haulView.morefail() //加载失败调用
                    //加载成功调用下面注释代码
                    haulView.moresuc() //加载成功调用
                    list.add("300")
//                    list.add("400")
//                    list.add("500")
//                    list.add("600")
                },3000)
            }

        }
    }
}