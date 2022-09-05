package com.quizapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.quizapp.R
import com.quizapp.api.ApiInterface
import com.quizapp.api.ApiUtilities
import com.quizapp.databinding.ActivityQuizBinding
import com.quizapp.model.Question
import com.quizapp.model.Quiz
import com.quizapp.model.Result
import com.quizapp.repository.QuizRepository
import com.quizapp.ui.fragment.McqFragment
import com.quizapp.viewmodel.QuizViewModel
import com.quizapp.viewmodel.QuizViewModelFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class QuizActivity : AppCompatActivity() {


    private  var questionsList = emptyList<Result>()
    private lateinit var quizViewModel : QuizViewModel
    lateinit var binding: ActivityQuizBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var  sharedViewModel: QuizViewModel

    private var NUM_PAGES = 6


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = binding.pager
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        initListeners()

        val apiInterface = ApiUtilities().getInstance().create(ApiInterface::class.java)
        val quizRepository = QuizRepository(apiInterface)
        quizViewModel =
            ViewModelProvider(this, QuizViewModelFactory(quizRepository))[QuizViewModel::class.java]

        sharedViewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        quizViewModel.quiz.observe(this, Observer {
            questionsList = listOf(it.result)
            countdown(questionsList)
        })
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem == NUM_PAGES-1) {
                results()
                Toast.makeText(this, "Quiz Finished", Toast.LENGTH_LONG).show();
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }

        binding.btnPrevious.setOnClickListener{
            if (viewPager.currentItem == 0) {
                Toast.makeText(this, "No More", Toast.LENGTH_LONG).show();
            } else {
                // Otherwise, select the previous step.
                viewPager.currentItem = viewPager.currentItem - 1
            }
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private fun countdown(questionsList: List<Result>) {

        val duration: Long = TimeUnit.MINUTES.toMillis(questionsList[0].timeInMinutes.toLong())
        val countTime = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                var sDuration: String = String.format(
                    Locale.ENGLISH,
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )

                binding.tvTimer.text = sDuration

            }

            override fun onFinish() {
                results()
            }
        }
        countTime.start()
    }

    private fun results() {
        val score = sharedViewModel.getScore().value
        val intent = Intent(this,ResultActivity::class.java)
        intent.putExtra("score",score)
        startActivity(intent)
    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = McqFragment(position)
}
}
