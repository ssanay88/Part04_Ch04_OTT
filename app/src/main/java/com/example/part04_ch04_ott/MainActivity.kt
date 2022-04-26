package com.example.part04_ch04_ott

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.part04_ch04_ott.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding    // 뷰바인딩

    private var isGateringMotionAnimating: Boolean = false
    private var isCurationMotionAnimation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeStatusBarTransparent()    // StatusBar 투명 처리 - Activity 클래스에 적용한 확장 함수
        initScrollViewListeners()    // 스크롤 뷰에 대한 인스턴스 생성
        initMotionLayoutListeners()    //
        initAppBar()    // Appbar 투명 처리
        initActionBar()    // 커스텀 ToolBar 적용
        initInsetMargin()    // 툴바에 안전한 마진을 적용





    }

    private fun initScrollViewListeners() {
        binding.scrollView.smoothScrollTo(0,0)

        // 모션뷰를 담고 있는 스크롤뷰에 대한 리스너 선언
        // viewTreeObserver : 뷰 트리의 전역 변경 사항을 알릴 수 있는 리스너를 등록
        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollValue = binding.scrollView.scrollY

            // 스크롤에 150만큼 내려갔을 경우
            if (scrollValue > 150f.dpToPx(this).toInt()) {
                // 애니메이션이 동작하고 있지 않을 경우만 새로운 애니메이션 시작
                if (isGateringMotionAnimating.not()) {    // 시작에서 끝으로
                    binding.gatheringDigitalThingsBackgroundMotionLayout.transitionToEnd()
                    binding.gatheringDigitalThingsLayout.transitionToEnd()
                    binding.buttonShownMotionLayout.transitionToEnd()
                }
                // 스크롤을 다시 위로 올렸을 경우
            } else {
                if (isGateringMotionAnimating.not()) {    // 끝에서 시작으로
                    binding.gatheringDigitalThingsBackgroundMotionLayout.transitionToStart()
                    binding.gatheringDigitalThingsLayout.transitionToStart()
                    binding.buttonShownMotionLayout.transitionToStart()
                }
            }

            if (scrollValue > binding.scrollView.height) {
                if (isCurationMotionAnimation.not()) {
                    binding.curationAnimationMotionLayout.setTransition(R.id.curation_animation_start1, R.id.curation_animation_end1)
                    binding.curationAnimationMotionLayout.transitionToEnd()
                    isCurationMotionAnimation = true
                }
            }

        }
    }

    private fun initMotionLayoutListeners() {
        // 모션 레이아웃에서 각 애니메이션이 시작될때의 리스너 함수들
        binding.gatheringDigitalThingsLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                isGateringMotionAnimating = true    // 애니메이션 시작할 때 애니메이션이 진행중임을 판단하는 변수 True 처리
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) = Unit

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                isGateringMotionAnimating = false    // 애니메이션이 종료될 경우 False 처리
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) = Unit

        })

        binding.curationAnimationMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) = Unit    // 애니메이션 동작 처리 유무를 앞에서 미리 판별했기 때문에 따로 처리하지 않는다.
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) = Unit

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.curation_animation_end1 -> {
                        binding.curationAnimationMotionLayout.setTransition(R.id.curation_animation_start2, R.id.curation_animation_end2)
                        binding.curationAnimationMotionLayout.transitionToEnd()
                    }
                }
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) = Unit

        })

    }

    // 앱바에서 스크롤 시 alpha값 변경
    private fun initAppBar() {
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 300f.dpToPx(this)    // 300까지는 여유를 둔다
            // 측정한 앱바 높이에서
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset = if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                binding.toolbarBackgroundView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            binding.toolbarBackgroundView.alpha = 1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        initActionBar()
    }

    // AppBar를 StatusBar만큼 띄어서 표시하는 함수, window에 있는 모든 시스템 영역의 Inset값을 조절
    private fun initInsetMargin() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(coordinator) {v: View, insets: WindowInsetsCompat ->
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = insets.systemWindowInsetBottom
            // 시스템에서 안전하게 줄수있는 margin으로 설정
            toolbarContainer.layoutParams = (toolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, insets.systemWindowInsetTop, 0, 0)
            }
            collapsingToolbarContainer.layoutParams = (collapsingToolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, 0, 0, 0)
            }

            insets.consumeSystemWindowInsets()
        }
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


// 확장 함수 - 기존 클래스에 원하는 기능을 추가하는 함수 각각 Float클래스와 Activity클래스에 추가
// dp단위를 Px로 변환
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
 - MotionLayout : ConstraintLayout의 서브클래스 , 애니메이션 기반 레이아웃
 - ConstrainitSet

2. 헤더 영역 - 어려움
 - App Bar
 - CollapsingToolbar
 - Inset(FitSystemWindow)

3. 모션 레이아웃 2차
 - MotionLayout
 - ConstrainitSet


 */