package com.example.bcsd_android_2025_1
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class RandomFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_random, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val textView = view.findViewById<TextView>(R.id.text_view)

        val count = arguments?.getInt("count_value")?:0//메인에서 받아온거
        val randomValue = (0..count).random()
        textView.text=randomValue.toString()

        parentFragmentManager.setFragmentResult(
            "random_result",
            bundleOf("random_value" to randomValue)
        )

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }

            }
        )

    }

}