package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import kotlin.random.Random

class SecondFragment : Fragment() {
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_second, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val textViewRandom = view.findViewById<TextView>(R.id.view_random)
        val buttonSelect = view.findViewById<Button>(R.id.button_select)

        count = arguments?.getInt("currentCount", 0) ?: 0
        val randomValue = Random.nextInt(0, count + 1)
        textViewRandom.text = "Random: $randomValue"

        buttonSelect.setOnClickListener {
            val result = Bundle().apply {
                putInt("selectedNumber", randomValue)
            }
            parentFragmentManager.setFragmentResult("randomResult", result)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
