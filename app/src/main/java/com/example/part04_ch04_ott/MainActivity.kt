package com.example.part04_ch04_ott

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.part04_ch04_ott.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isGateringMotionAnimating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeStatusBarTransparent()    // StatusBar 투명 처리
        initActionBar()    // 커스텀 ToolBar 적용

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (binding.scrollView.scrollY > 150f.dpToPx(this).toInt()) {
                if (isGateringMotionAnimating.not()) {
                    binding.gatheringDigitalThingsLayout.transitionToEnd()
                    binding.buttonShownMotionLayout.transitionToEnd()
                }
            } else {
                if (isGateringMotionAnimating.not()) {
                    binding.gatheringDigitalThingsLayout.transitionToStart()
                    binding.buttonShownMotionLayout.transitionToStart()
                }
            }
        }

        binding.gatheringDigitalThingsLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                isGateringMotionAnimating = true
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) = Unit

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                isGateringMotionAnimating = false
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) = Unit

        })

    }

    // AppBar를 StatusBar만큼 띄어서 표시하는 함수
    private fun initAppBar() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 120f.dpToPx(this)
            val abstractOffset = abs(verticalOffset)
            if (abstractOffset < topPadding) {
                binding.toolbarBackgroundView.alpha = 0f
                return@OnOffsetChangedListener
            }
            val verticalOffsetByTopPadding = abstractOffset - topPadding
            val percentage = abs(verticalOffsetByTopPadding) / appBarLayout.totalScrollRange
            binding.toolbarBackgroundView.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        initActionBar()
    }

    // ToolBar를 커스터마이징
    private fun initActionBar() = with(binding) {
        toolbar.navigationIcon = null
        toolbar.setContentInsetsAbsolute(0,0)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }
    }


}



fun Float.dpToPx(context: Context): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

// StatusBar를 투명하게 하는 확장함수
fun Activity.makeStatusBarTransparent() {
    window.apply {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
    }
}




/*

1. 모션레이아웃 1차
 - ScrollView
 - MotionLayout
 - ConstrainitSet

2. 헤더 영역
 - App Bar
 - CollapsingToolbar
 - Inset(FitSystemWindow)

3. 모션 레이아웃 2차
 - MotionLayout
 - ConstrainitSet


 */