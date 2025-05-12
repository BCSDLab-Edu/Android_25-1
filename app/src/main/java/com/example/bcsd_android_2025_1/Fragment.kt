package com.example.bcsd_android_2025_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class RandomFragment : Fragment() {
     private var randomData : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            randomData = it.getInt("randomData", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_random,container,false)

        val randomNum = (0..randomData).random()

        val tvResult: TextView = view.findViewById(R.id.tv_main2_num)
        tvResult.text = randomNum.toString()

        val intent = Intent().apply{

            putExtra("randomNum", randomNum)
        }
        requireActivity().setResult(AppCompatActivity.RESULT_OK, intent)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(randomData: Int) =
            RandomFragment().apply {
                arguments = Bundle().apply {
                    putInt("randomData", randomData)
                }
            }
    }
}