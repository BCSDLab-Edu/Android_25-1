package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.widget.EditText


class FirstFragment : Fragment() {
    private var count = 0
    private lateinit var countTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_first, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        countTextView = view.findViewById(R.id.textViewCount)
        val toastBtn = view.findViewById<Button>(R.id.toast)
        val countBtn = view.findViewById<Button>(R.id.count)
        val randomBtn = view.findViewById<Button>(R.id.random)
        val dialogBtn = view.findViewById<Button>(R.id.dialog)

        toastBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Hello!", Toast.LENGTH_SHORT).show()
        }

        countBtn.setOnClickListener {
            count++
            countTextView.text = count.toString()
        }

        randomBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("currentCount", count)
            }
            val secondFragment = SecondFragment().apply { arguments = bundle }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, secondFragment)
                .addToBackStack(null)
                .commit()
        }

        dialogBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Basic Dialog")
                .setMessage("Choice one")
                .setIcon(R.mipmap.ic_launcher)

            val listener = DialogInterface.OnClickListener { dialog, p1 ->
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        count = 0
                        countTextView.text = count.toString()
                        dialog.dismiss()
                    }

                    DialogInterface.BUTTON_NEUTRAL -> {
                        Toast.makeText(requireContext(), "Spread Luv!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }

            builder.setPositiveButton("reset", listener)
            builder.setNeutralButton("toast", listener)
            builder.setNegativeButton("cancel", listener)

            builder.show()
        }

        setFragmentResultListener("randomResult") { _, result ->
            val newCount = result.getInt("selectedNumber", count)
            count = newCount
            countTextView.text = count.toString()
        }
    }
}
