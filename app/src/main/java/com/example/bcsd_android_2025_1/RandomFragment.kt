package com.example.bcsd_android_2025_1

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf

class RandomFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_random, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textviewSecondNumber: TextView = view.findViewById<TextView>(R.id.textview_second_number)
        val textviewSecondInformation: TextView = view.findViewById<TextView>(R.id.textview_second_information)

        var randomNumber = 0
        if(arguments?.containsKey("countNumber") == true){
                val count = arguments?.getInt("countNumber")!!
                randomNumber = (0 until count+1).random()
            textviewSecondNumber.text = randomNumber.toString()
            textviewSecondInformation.text = getString(R.string.text_information, count)
        }

        val bundleRandom = bundleOf("keyRandomNumber" to randomNumber)
        parentFragmentManager.setFragmentResult("requestRandomNumber", bundleRandom)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        requireActivity().supportFragmentManager.findFragmentByTag("MAIN_FRAGMENT_TAG")?.childFragmentManager
                            ?.beginTransaction()
                            ?.remove(this@RandomFragment)
                            ?.commit()
                    }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}