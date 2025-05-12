package com.example.bcsd_android_2025_1
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainFragment : Fragment(R.layout.fragment_main) {
    var count = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button_toast = view.findViewById<Button>(R.id.button_toast)

        button_toast.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setPositiveButton("positive") { dialog, which ->
                count = 0
            }
            builder.setNeutralButton("neutral") { dialog, which ->
                Toast.makeText(requireActivity(), getString(R.string.toast_message), Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("negative") { dialog, which ->
                requireActivity().finish()
            }
            builder.show()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }

    }
}
