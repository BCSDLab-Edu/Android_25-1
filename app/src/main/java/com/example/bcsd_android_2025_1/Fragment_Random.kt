package com.example.bcsd_android_2025_1
import androidx.activity.OnBackPressedCallback
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.random.Random

class Fragment1 : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        )

        val count = arguments?.getInt("currentCount") ?: 0
        val randomNumber = Random.nextInt(0, count + 1)
        val randTextView: TextView = view.findViewById(R.id.textview_random)

        randTextView.text = randomNumber.toString()
        val result = Bundle().apply {
            putInt("randomNumber", randomNumber)
        }
        parentFragmentManager.setFragmentResult("randomResult", result)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }
}