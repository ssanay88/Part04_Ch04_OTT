package com.example.part04_ch04_ott

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.part04_ch04_ott.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isGateringMotionAnimating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scrollView.viewTreeObserve.

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