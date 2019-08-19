package com.jeeb.mycommunity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import com.jeeb.mycommunity.databinding.ActivityOnboardBinding


class OnBoardActivity : AppCompatActivity() {
    lateinit var binding : ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboard)
    }

    override fun onCreateSupportNavigateUpTaskStack(builder: TaskStackBuilder) {
        super.onCreateSupportNavigateUpTaskStack(builder)
    }
}