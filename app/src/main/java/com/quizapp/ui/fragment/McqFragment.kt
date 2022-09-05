package com.quizapp.ui.fragment

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quizapp.R
import com.quizapp.api.ApiInterface
import com.quizapp.api.ApiUtilities
import com.quizapp.databinding.FragmentMcqBinding
import com.quizapp.model.Result
import com.quizapp.repository.QuizRepository
import com.quizapp.viewmodel.QuizViewModel
import com.quizapp.viewmodel.QuizViewModelFactory
import java.util.*
import java.util.concurrent.TimeUnit

class McqFragment(val position : Int) : Fragment() {

    //private lateinit var questionsList : List<Result>
    private  var questionsList = emptyList<Result>()
    private lateinit var quizViewModel : QuizViewModel
    private lateinit var binding: FragmentMcqBinding
    private  var mutableList = mutableListOf<Int>()
    var isSelectedBtn1 : Boolean = false
    var isSelectedBtn2 : Boolean = false
    var isSelectedBtn3 : Boolean = false
    var isSelectedBtn4 : Boolean = false
    var isSelected : Boolean = false
    var selectedOpt : Int? = null
    private val sharedViewModel: QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMcqBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiInterface = ApiUtilities().getInstance().create(ApiInterface::class.java)

        val quizRepository = QuizRepository(apiInterface)
        quizViewModel =
            ViewModelProvider(this, QuizViewModelFactory(quizRepository))[QuizViewModel::class.java]

        quizViewModel.quiz.observe(viewLifecycleOwner, Observer {
            questionsList = listOf(it.result)
            setData(questionsList)
            //countdown(questionsList)
        })

       /* binding.button1.setOnClickListener {

            val mutableList = selectedOptList.toMutableList()
            if (isSelectedBtn1){
                isSelectedBtn1 = false
                isSelected = true
                if (mutableList.contains(1)){
                    mutableList.remove(1)
                }
                binding.button1.setBackgroundColor(Color.WHITE)
                binding.save.setBackgroundColor(Color.CYAN)
            }else{
                isSelectedBtn1 = true
                isSelected = true
                selectedOpt = 1
                selectedOptList = listOf(1)
                binding.button1.setBackgroundColor(Color.BLUE)
                binding.save.setBackgroundColor(Color.GREEN)
            }
        }
        binding.button2.setOnClickListener {
            isSelectedBtn2 = true
            isSelected = true
            selectedOpt = 2
            selectedOptList= listOf(2)
            binding.button2.setBackgroundColor(Color.BLUE)
            binding.save.setBackgroundColor(Color.GREEN)
        }
        binding.button3.setOnClickListener {
            isSelectedBtn3 = true
            isSelected = true
            selectedOpt = 3
            selectedOptList= listOf(3)
            binding.button3.setBackgroundColor(Color.BLUE)
            binding.save.setBackgroundColor(Color.GREEN)
        }
        binding.button4.setOnClickListener {
            isSelectedBtn4 = true
            isSelected = true
            selectedOpt = 4
            selectedOptList= listOf(4)
            binding.button4.setBackgroundColor(Color.BLUE)
            binding.save.setBackgroundColor(Color.GREEN)
        }*/
        binding.button1.setOnClickListener {

            if (isSelectedBtn1){
                isSelectedBtn1 = false
                isSelected = false
                if (mutableList.contains(1)){
                    mutableList.remove(1)
                }
                binding.button1.setBackgroundColor(Color.WHITE)
                binding.save.setBackgroundColor(Color.GREEN)
            }else{
                isSelectedBtn1 = true
                isSelected = true
                selectedOpt = 1
                mutableList.add(1)
                binding.button1.setTextColor(Color.BLACK)
                binding.button1.setBackgroundColor(Color.CYAN)
                binding.save.setBackgroundColor(Color.GREEN)
            }
        }
        binding.button2.setOnClickListener {

            if (isSelectedBtn2){
                isSelectedBtn2 = false
                isSelected = false
                if (mutableList.contains(2)){
                    mutableList.remove(2)
                }

                binding.button2.setBackgroundColor(Color.WHITE)
                binding.save.setBackgroundColor(Color.GREEN)
            }else{
                isSelectedBtn2 = true
                isSelected = true
                selectedOpt = 2
                mutableList.add(2)
                binding.button2.setTextColor(Color.BLACK)
                binding.button2.setBackgroundColor(Color.CYAN)
                binding.save.setBackgroundColor(Color.GREEN)
            }
        }
        binding.button3.setOnClickListener {

           // val mutableList = selectedOptList.toMutableList()
            if (isSelectedBtn3){
                isSelectedBtn3 = false
                isSelected = false
                if (mutableList.contains(3)){
                    mutableList.remove(3)
                }
                binding.button3.setBackgroundColor(Color.WHITE)
                binding.save.setBackgroundColor(Color.GREEN)
            }else{
                isSelectedBtn3 = true
                isSelected = true
                selectedOpt = 3
                mutableList.add(3)
                binding.button3.setTextColor(Color.BLACK)
                binding.button3.setBackgroundColor(Color.CYAN)
                binding.save.setBackgroundColor(Color.GREEN)
            }
        }
        binding.button4.setOnClickListener {

           // val mutableList = selectedOptList.toMutableList()
            if (isSelectedBtn4){
                isSelectedBtn4 = false
                isSelected = false
                if (mutableList.contains(4)){
                    mutableList.remove(4)
                }
                binding.button4.setBackgroundColor(Color.WHITE)
                binding.save.setBackgroundColor(Color.GREEN)
            }else{
                isSelectedBtn4 = true
                isSelected = true
                selectedOpt = 4
                mutableList.add(4)
                binding.button4.setTextColor(Color.BLACK)
                binding.button4.setBackgroundColor(Color.CYAN)
                binding.save.setBackgroundColor(Color.GREEN)
            }
        }
        binding.save.setOnClickListener {
            val correctOptionsList = questionsList[0].questions[position].correct_answers
            val size = correctOptionsList.size
            if (!isSelected) {
                Toast.makeText(activity, "Please choose the option", Toast.LENGTH_SHORT).show()
            } else {
                if (mutableList.size == size){
                    if(mutableList.containsAll(correctOptionsList)){
                        sharedViewModel.updateScore(1)
                        Log.i("MCQDEBUG", "onViewCreated:  " + quizViewModel.getScore().value.toString())
                    }
                }
                for (i in 0 until size){
                    when(correctOptionsList[i]){
                        1 -> binding.button1.setBackgroundColor(Color.GREEN)
                        2 -> binding.button2.setBackgroundColor(Color.GREEN)
                        3 -> binding.button3.setBackgroundColor(Color.GREEN)
                        4 -> binding.button4.setBackgroundColor(Color.GREEN)
                    }
                }
                if (!correctOptionsList.contains(1)){
                    binding.button1.setBackgroundColor(Color.RED)
                }
                if(!correctOptionsList.contains(2)){
                    binding.button2.setBackgroundColor(Color.RED)
                }
                if(!correctOptionsList.contains(3)){
                    binding.button3.setBackgroundColor(Color.RED)
                }
                if(!correctOptionsList.contains(4)){
                    binding.button4.setBackgroundColor(Color.RED)
                }
                binding.button1.isEnabled = false
                binding.button2.isEnabled = false
                binding.button3.isEnabled = false
                binding.button4.isEnabled = false
                binding.save.isEnabled = false
                binding.save.visibility = View.INVISIBLE
            }
        }
    }
/*
        binding.button1.setOnClickListener {
            checkAnswer(1)

        }
        binding.button2.setOnClickListener {
            checkAnswer(2)
        }
        binding.button3.setOnClickListener {
            checkAnswer(3)
        }
        binding.button4.setOnClickListener {
            checkAnswer(4)
        }
    }

    private fun checkAnswer(selection : Int) {

        val correctOptionsList = questionsList[0].questions[position].correct_answers

        if (correctOptionsList.contains(selection)){
            when(selection){
                1 -> binding.button1.setBackgroundColor(Color.GREEN)
                2 -> binding.button2.setBackgroundColor(Color.GREEN)
                3 -> binding.button3.setBackgroundColor(Color.GREEN)
                4 -> binding.button4.setBackgroundColor(Color.GREEN)
            }
        }
        else{
            when(selection){
                1 -> binding.button1.setBackgroundColor(Color.RED)
                2 -> binding.button2.setBackgroundColor(Color.RED)
                3 -> binding.button3.setBackgroundColor(Color.RED)
                4 -> binding.button4.setBackgroundColor(Color.RED)
            }
        }

    }
    */

        private fun setData(questionsList: List<Result>) {
            binding.tvQuestion.text = questionsList[0].questions[position].lable
            binding.button1.text = questionsList[0].questions[position].options[0].lable
            binding.button2.text = questionsList[0].questions[position].options[1].lable
            binding.button3.text = questionsList[0].questions[position].options[2].lable
            binding.button4.text = questionsList[0].questions[position].options[3].lable
            binding.pbQuiz.visibility = View.INVISIBLE

        }

/*
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
                }
            }
            countTime.start()
        }*/
}