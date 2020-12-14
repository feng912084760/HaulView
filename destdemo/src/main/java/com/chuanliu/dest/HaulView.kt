package com.chuanliu.dest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class HaulView : LinearLayout {
    constructor(context: Context):super(context)
    constructor(context :Context, attr : AttributeSet): super(context, attr)
    var recyView = EnableStopRecyView(context)
    var freshView:View? = null
    var freshTv:TextView? = null
    var freshPb:ProgressBar? = null
//    private var freshView:TextView = TextView(context)
//    private var moreView: TextView = TextView(context)
    var moreView:View? = null
    var moreTv:TextView? = null
    var morePb:ProgressBar? = null
    private var firstLayout = true
    var moreHeight = -1
    var moreTvH = -1
    private var freshHeight = -1
    private var lastY : Float = -1.0F
    private val tAG = "HaulView"

    init {

//        freshView.text = "下拉刷新"
//        moreView.text = "上拉更多"
        addHeaderFooterView()
        recyView.overScrollMode = View.OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener{
            if (firstLayout) {
                firstLayout = false
                freshHeight = freshView!!.height
                freshView?.setPadding(0,-freshHeight,0,0)
                moreView?.measure(0, 0)
                moreHeight = moreView!!.measuredHeight
                moreTvH = moreTv!!.measuredHeight
//                moreTv?.bottom = moreHeight
//                morePb?.bottom = moreHeight
//                moreView.setPadding(0,-moreHeight,0,0)
                Log.e(tAG,"freshHeight:$freshHeight,moreHeight:$moreHeight,moreTvHeight:${moreTv?.measuredHeight}，" +
                        ",moreTvWidth:${moreTv?.measuredWidth}")
                moreTv?.layoutParams?.width = moreTv!!.measuredWidth
//                freshView.postDelayed({freshView.top = 1410},3000)
//                freshView.postDelayed({recyView.offsetTopAndBottom(-50)},6000)

            }
        }
    }
    //添加头部尾部
    fun addHeaderFooterView() {
        freshView = LayoutInflater.from(context).inflate(R.layout.fresh,this,false)
        freshTv = freshView?.findViewById<TextView>(R.id.freshTv)
        freshPb = freshView?.findViewById<ProgressBar>(R.id.freshPb)
//        val layF = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//        freshView.layoutParams = layF
//        freshView.gravity = Gravity.CENTER
        addView(freshView)
        val layRv = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        addView(recyView,layRv)
//        val layM = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//        moreView.layoutParams = layM
//        moreView.gravity = Gravity.CENTER
//        moreView.textSize = 30.0F
        moreView = LayoutInflater.from(context).inflate(R.layout.more,this,false)
        moreTv = moreView?.findViewById<TextView>(R.id.moreTv)
        morePb = moreView?.findViewById<ProgressBar>(R.id.morePb)
        addView(moreView)
    }

    fun setLayoutManager(layMana : LinearLayoutManager) {
        recyView.layoutManager = layMana
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e(tAG, "dispatchTouchEvent")
        if (event != null) {
            Log.e(tAG, "dispatchTouchEvent event.action" + event.action)
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 正在加载的过程中，不再重复响应触摸事件
                    Log.e(tAG, "${freshView?.paddingTop},${moreView?.top},$height")
                    if (freshView!!.paddingTop > -freshHeight
                        || moreView!!.top < height
                    ) {
                        return false
                    }
                    lastY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val cY = event.y
                    val disY = cY - lastY
                    Log.e(tAG, "滑动距离$disY")
//                    if (disY > 0) { //向下滑动
                    val pos = getFirstVisibleView()
                    Log.e(tAG, "向下滑动$pos")

                    // 处理数据为0时的滑动
                    if (pos == -1) {
                      if (recyView.top == paddingTop)  {
                          if (disY > 0) { //向下滑动
                              recyView.stopScroll = true
                              var bPad = freshView!!.paddingTop + disY.toInt()
                              if (bPad > 0)
                                  bPad = 0
                              freshView!!.setPadding(0,bPad,0, 0)
                          } else {
                              val loc = moreView!!.top + disY.toInt()
                              recyView.offsetTopAndBottom(disY.toInt())
                              moreView?.top = loc
                              if (moreTv?.text!! != "上拉更多") {
                                  moreTv?.text = "上拉更多"
                              }
                          }
                      } else if (recyView.top < paddingTop){// 处于上拉更多的状态
                          recyView.stopScroll = true

                          val loc = moreView!!.top + disY.toInt()
                          if (loc <= height-moreHeight- paddingBottom) {
                              recyView.offsetTopAndBottom(height-moreHeight- paddingBottom - moreView!!.top)
                              moreView?.top = height-moreHeight- paddingBottom
                              moreTv?.text = "松手加载"
                          } else if (loc > height- paddingBottom) {
                              recyView.offsetTopAndBottom(height- paddingBottom - moreView!!.top)
                              moreView?.top = height- paddingBottom
                              Log.e(tAG, "loc > height- paddingBottom moreView还原")
                          } else {
//                                        freshView?.offsetTopAndBottom(disY.toInt())
                              recyView.offsetTopAndBottom(disY.toInt())
                              moreView?.top = loc
                              if (moreTv?.text!! != "上拉更多") {
                                  moreTv?.text = "上拉更多"
                              }
                              if (moreTv?.bottom == 0) {
                                  morePb?.bottom = moreHeight
                                  moreTv?.top = (moreHeight - moreTvH)/2
                                  moreTv?.bottom=moreHeight/2 + moreTvH/2
                              }
//                              Log.e(tAG, "else 继续${moreView?.top}")
                          }
                      } else {// 处于下拉刷新的状态
                          var bPad = freshView!!.paddingTop + disY.toInt()
                          if (bPad >= 0) {
                              bPad = 0
                              freshTv!!.text = "松手加载"
                          } else if (bPad < -freshHeight){
                              bPad = -freshHeight
                              freshTv!!.text = "下拉刷新"
                          } else {
                              if (freshTv!!.text != "下拉刷新") {
                                  freshTv!!.text = "下拉刷新"
                              }
                          }
                          freshView?.setPadding(0,bPad,0, 0)
                          recyView.stopScroll = true
                      }
                    }

                    if (pos == 0) {
                        val firView = recyView.getChildAt(pos)
                        Log.e(tAG, "firView.top:${firView.top},freshHeight$freshHeight,recyView.top${recyView.top}")
                        if (recyView.top == paddingTop && firView.top == recyView.paddingTop) {
                            if (disY > 0) { //向下滑动
                                recyView.stopScroll = true
                                var bPad = freshView!!.paddingTop + disY.toInt()
                                if (bPad > 0)
                                    bPad = 0
                                freshView!!.setPadding(0,bPad,0, 0)
                            } else {
                                recyView.stopScroll = false
                            }
                        } else if (recyView.top in paddingTop..paddingTop+freshHeight && firView.top == recyView.paddingTop) {
                            var bPad = freshView!!.paddingTop + disY.toInt()
                            if (bPad >= 0) {
                                bPad = 0
                                freshTv!!.text = "松手加载"
                            } else if (bPad < -freshHeight){
                                bPad = -freshHeight
                                freshTv!!.text = "下拉刷新"
                            } else {
                                if (freshTv!!.text != "下拉刷新") {
                                    freshTv!!.text = "下拉刷新"
                                }
                            }
                            freshView?.setPadding(0,bPad,0, 0)
                            recyView.stopScroll = true
                        } else {
                            recyView.stopScroll = false
                        }

                    }

//                    } else { //向上滑动
                    val posLas = getLastVisibleView()
                    Log.e(tAG, "向上滑动${posLas.contentToString()}")
                    val lastPos = (recyView.adapter?.itemCount ?: 1) - 1
                    var isLastLine = false
                    for (pi in posLas) {
                        if (lastPos!=-1 && pi==lastPos) {
                            isLastLine = true
                            break
                        }
                    }

                    if (isLastLine) {
                        var bottomMax = posLas[0]
                        if (posLas.size > 1) {
                            for (i in 1 until posLas.size) {
                                if (recyView.getChildAt(posLas[i]-pos).bottom > recyView.getChildAt(bottomMax-pos).bottom) {
                                    bottomMax = posLas[i]
                                }
                            }
                        }

                        val lastView = recyView.getChildAt(bottomMax-pos)
                        Log.e(tAG, "lastView.bottom:${lastView.bottom},recyView.height${recyView.height},height:${height}")
                        if (lastView.bottom in 0 ..recyView.height) {
                            Log.e(tAG, "moreHeight:${moreHeight},disY${disY},moreView.top:${moreView?.top}moreView.bottom:${moreView?.bottom}")
                            if (moreView?.top == height-moreHeight - paddingBottom) {
                                Log.e(tAG, "1")
                                if (disY > 0) {
                                    recyView.stopScroll = true
                                    recyView.offsetTopAndBottom(disY.toInt())
                                    moreView?.top = (moreView!!.top + disY.toInt())
                                }
                            } else if (moreView?.top == height- paddingBottom) {
                                Log.e(tAG, "2")
                                if (disY < 0) {
                                    recyView.stopScroll = true
//                                        freshView?.offsetTopAndBottom(disY.toInt())
                                    recyView.offsetTopAndBottom(disY.toInt())
                                    moreView?.top = moreView!!.top + disY.toInt()
//                                        moreTv?.text = "上拉更多"
                                    morePb?.bottom = moreHeight
//                                        moreTv?.top = (moreHeight - moreTvH)/2
//                                        moreTv?.bottom=moreHeight/2 + moreTvH/2
                                    Log.e(tAG, "2，moreView?.top${moreView?.top}")
                                } else {
                                    recyView.stopScroll = false
                                    Log.e(tAG, "stopScroll = false")
                                }
                            } else if (moreView?.top in height-moreHeight- paddingBottom ..height- paddingBottom) {
                                Log.e(tAG, "3")
                                recyView.stopScroll = true

                                val loc = moreView!!.top + disY.toInt()
                                if (loc <= height-moreHeight- paddingBottom) {
                                    recyView.offsetTopAndBottom(height-moreHeight- paddingBottom - moreView!!.top)
                                    moreView?.top = height-moreHeight- paddingBottom
                                    moreTv?.text = "松手加载"
                                } else if (loc > height- paddingBottom) {
                                    recyView.offsetTopAndBottom(height- paddingBottom - moreView!!.top)
                                    moreView?.top = height- paddingBottom
                                    Log.e(tAG, "loc > height- paddingBottom moreView还原")
                                } else {
//                                        freshView?.offsetTopAndBottom(disY.toInt())
                                    recyView.offsetTopAndBottom(disY.toInt())
                                    moreView?.top = loc
                                    if (moreTv?.text!! != "上拉更多") {
                                        moreTv?.text = "上拉更多"
                                    }
                                    if (moreTv?.bottom == 0) {
                                        morePb?.bottom = moreHeight
                                        moreTv?.top = (moreHeight - moreTvH)/2
                                        moreTv?.bottom=moreHeight/2 + moreTvH/2
                                    }
                                    Log.e(tAG, "else 继续${moreView?.top}")
                                }
                            } else if (moreView!!.top < height-moreHeight- paddingBottom) {
                                Log.e(tAG, "4")
                                recyView.offsetTopAndBottom(height-moreHeight- paddingBottom - moreView!!.top)
                                moreView?.top = height-moreHeight- paddingBottom
                            } else {
                                Log.e(tAG, "5")
                                Log.e(tAG, "else moreView还原")
                                recyView.offsetTopAndBottom(height- paddingBottom - moreView!!.top)
                                moreView?.top = height- paddingBottom
                            }
                        }
                    }
                    lastY = cY
                }
                MotionEvent.ACTION_UP -> {
                    recyView.stopScroll = false
                    if (freshView?.paddingTop == 0) {
//                        postDelayed(runFresh, 3000)
                        freshTv?.text = "刷新中"
                        freshPb?.visibility = View.VISIBLE
                        resultCall?.onFresh()
                    } else if (freshView!!.paddingTop > -freshHeight) {
                        postDelayed(runFresh, 20)
                    }

                    if (moreView?.top  == height-moreHeight) {
//                        postDelayed(runMore, 3000)
                        moreTv?.text = "加载中"
                        morePb?.visibility = View.VISIBLE
                        resultCall?.onMore()
                    } else if (moreView!!.top < height) {
                        postDelayed(runMore, 20)
                    }
                }
            }



        } else {
            Log.e(tAG, "dispatchTouchEvent event==null")
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e(tAG, "onTouchEvent")

        return super.onTouchEvent(event)
    }

    private fun getFirstVisibleView():Int {
        var p = -1
        if (recyView.layoutManager is LinearLayoutManager) {
            p = (recyView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else if (recyView.layoutManager is StaggeredGridLayoutManager) {
            var res = (recyView.layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(null)
            Log.e(tAG, "getFirstVisibleView${res.contentToString()}")
            p = res[0]
            if (res.size > 1) {
                for (ind in 1 until res.size) {
                    if (p > res[ind])
                       p = res[ind]
                }
            }
        }
        return p
    }
    private fun getLastVisibleView():IntArray {
        if (recyView.layoutManager is LinearLayoutManager) {
            var p = (recyView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            return IntArray(1) { p }
        } else if (recyView.layoutManager is StaggeredGridLayoutManager) {
            var res = (recyView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            return res
        }
        return IntArray(0)
    }
    fun freshSuc() {
        freshTv?.text = "刷新完成"
        freshPb?.visibility = View.INVISIBLE
        postDelayed(
            runFresh
            ,2000)
    }
    fun freshFail() {
        freshTv?.text = "刷新失败"
        freshPb?.visibility = View.INVISIBLE
        postDelayed(
            runFresh
            ,2000)
    }
    fun dismissFresh() {
        post(runFresh)
    }
    private val runFresh:Runnable = object:Runnable {
        override fun run() {
            val padTop = freshView!!.paddingTop
            if (padTop > -freshHeight) {
                var padMinute = padTop - 15
                if (padMinute < -freshHeight) {
                    padMinute = -freshHeight
                    freshView?.setPadding(0,padMinute,0,0)
                } else {
                    freshView?.setPadding(0,padMinute,0,0)
                    postDelayed(this, 20)
                }
                if (padMinute == -freshHeight) {
                    freshPb?.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun moresuc() {
        post {
            moreTv?.text = "加载完成"
            morePb?.visibility = View.INVISIBLE

        }
        postDelayed(
            runMoreSuc
        ,2000)
    }
    fun morefail() {
        post {
            moreTv?.text = "加载失败"
            morePb?.visibility = View.INVISIBLE

        }
        postDelayed(
            runMore
            ,2000)
    }
    fun dismissMore() {
        post(runMore)
    }
    private val runMoreSuc:Runnable = Runnable {
        Log.e(tAG, "runMore")
        val fvTop = moreView!!.top

        morePb?.visibility = View.INVISIBLE
        recyView.offsetChildrenVertical(-moreHeight)//向上偏移
        recyView.adapter?.notifyDataSetChanged()
        recyView.offsetTopAndBottom(height-fvTop)
        moreView?.top = height
    }

    private val runMore:Runnable = object:Runnable {
        override fun run() {
            Log.e(tAG, "runMore")
            val fvTop = moreView!!.top
            if (fvTop < height) {
                var fvTopAdd = fvTop + 15
                if (fvTopAdd > height) {
                    fvTopAdd = height
                    recyView.offsetTopAndBottom(height-fvTop)
                    moreView?.top = fvTopAdd

                } else {
                    recyView.offsetTopAndBottom(15)
                    moreView?.top = fvTopAdd
                    postDelayed(this, 20)
                }
                if (fvTopAdd == height) {
                    morePb?.visibility = View.INVISIBLE
                }

            }
        }
    }

    var resultCall : ResultCall? = null



    interface ResultCall {
        fun onFresh()
        fun onMore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(tAG, "onMeasure moreView.top:${moreView?.top}")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.e(tAG, "onLayout moreView.top:${moreView?.top}")
    }

}