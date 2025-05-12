package com.example.bcsd_android_2025_1
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    var count = 0
    private lateinit var countText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        countText = view.findViewById(R.id.text_view)
        view.findViewById<Button>(R.id.button_toast).setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("I am the message")
                .setTitle("I am the title")
                .setPositiveButton("positive") { dialog, _ ->
                    count = 0
                    countText.text = count.toString()
                    dialog.dismiss()
                }
                .setNeutralButton("neutral") { dialog, _ ->
                    Toast.makeText(requireContext(), "우진업!!!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("negative") { dialog, _ ->
                    dialog.dismiss()
                }
                    .show()
        }
        view.findViewById<Button>(R.id.button_count).setOnClickListener {
            count++
            countText.text = count.toString()
        }

        parentFragmentManager.setFragmentResultListener("random_result", viewLifecycleOwner) //얘는 받는거?
        { _, bundle ->
            val randomValue = bundle.getInt("random_value",0)
            count = randomValue
            countText.text = count.toString()
        }

        view.findViewById<Button>(R.id.button_random).setOnClickListener { // 랜덤으로 보내기
            val fragment = RandomFragment().apply(){
                arguments = bundleOf("count_value" to count)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}