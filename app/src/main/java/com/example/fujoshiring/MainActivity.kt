package com.example.fujoshiring

import android.animation.ObjectAnimator
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fujoshiring.databinding.ActivityMainBinding

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var animator: ObjectAnimator
    private lateinit var soundPool: SoundPool
    private var alertId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        // build soundPool and load music
        soundPool = SoundPool.Builder().setMaxStreams(10).build()
        alertId = soundPool.load(this, R.raw.alarm_clock_sound, 0)
        // setup button
        binding.ringingButton.setOnClickListener(ringingButtonListenerHandler)
    }

    private val ringingButtonListenerHandler = View.OnClickListener {
        if (binding.ringingButton.text == "開啟腐女鈴") {
            // 避免點擊太快的緩衝
            Thread.sleep(100)
            // 文字變化
            binding.ringingButton.text = "關閉腐女鈴"
            // 使 ringImageView 震動
            animator = ObjectAnimator.ofFloat(binding.ringImageView, "translationX", 0f, -30f, 0f, 30f, 0f)
            animator.duration = 10
            animator.repeatCount = ObjectAnimator.INFINITE
            animator.start()
            // 使手機發出鈴聲
            playMusic(alertId)
            // debugging log
            Log.d(TAG, "當開啟腐女鈴的時候")
        }
        else {
            // 避免點擊太快的緩衝
            Thread.sleep(100)
            // 文字變化
            binding.ringingButton.text = "開啟腐女鈴"
            // 使 ringImageView 停止震動
            animator.cancel()
            // 使手機停止發出鈴聲
            stopMusic(alertId)
            // 懷疑 raw 的 id 會一直變化，將其重新 assign 後可以解決第二次 stop 沒有辦法停下來的問題
            alertId = soundPool.load(this, R.raw.alarm_clock_sound, 0)
            // debugging log
            Log.d(TAG, "當開啟腐女鈴的時候")
        }
    }

    private fun playMusic(alertId: Int) {
        soundPool.play(alertId, 2.0f, 2.0f, 0,200000, 1.0f)
    }

    private fun stopMusic(alertId: Int) {
        soundPool.stop(alertId)
    }
}