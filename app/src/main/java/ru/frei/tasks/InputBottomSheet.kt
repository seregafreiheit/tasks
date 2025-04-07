package ru.frei.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.frei.tasks.databinding.FragmentTaskAddListDialogBinding

abstract class InputBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskAddListDialogBinding? = null
    private val binding get() = _binding!!
    private val editText get() = binding.addEditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskAddListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun setupInput(
        initialText: String? = null,
        onActionDone: (String) -> Unit
    ) {
        editText.apply {
            requestFocus()
            initialText?.let { setText(it) }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val text = text.toString()
                    if (text.isNotBlank()) {
                        onActionDone(text)
                    }
                    dismiss()
                    true
                } else {
                    false
                }
            }
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}