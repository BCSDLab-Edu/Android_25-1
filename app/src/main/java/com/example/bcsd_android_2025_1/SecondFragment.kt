package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import kotlin.random.Random

class SecondFragment : Fragment() {
    private var count : Int = 0

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            count = it.getInt(ARG_COUNT, 0)
        }

        val back = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, back)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val randomNumber = Random.nextInt(0, count + 1)

        view.findViewById<TextView>(R.id.textview_random_info).text =
            "Here is a random number between 0 and $count"
        view.findViewById<TextView>(R.id.textview_random_number).text =
            randomNumber.toString()

        parentFragmentManager.setFragmentResult(
            "randomResult",
            Bundle().apply {
                putInt("randomNumber", randomNumber)
            }
        )
    }

    companion object {
        private const val ARG_COUNT = "count"

        fun newInstance(count: Int): SecondFragment {
            val fragment = SecondFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_COUNT, count)
            fragment.arguments = bundle
            return fragment
        }
    }
}