package ru.frei.tasks;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ru.frei.tasks.data.DatabaseHelper;
import ru.frei.tasks.data.ListsEntry;
import ru.frei.tasks.databinding.FragmentTaskAddListDialogBinding;

public class AddListFragment extends BottomSheetDialogFragment {

    private long listId;
    private EditText editText;
    private FragmentTaskAddListDialogBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_add_list_dialog, container, false);

        listId = getArguments().getLong("listId", 0);

        editText = binding.addEditText;
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String list = editText.getText().toString();
                    if (list.equals("")) {
                        dismiss();
                        return false;
                    }
                    if (listId == 0) {
                        saveList(list);
                    } else {
                        renameList(list);
                    }
                }
                return true;
            }
        });
    }

    private void saveList(String list) {
        final ListsEntry listsEntry = new ListsEntry(list);
        listId = DatabaseHelper.insertList(listsEntry);
        dismiss();
    }

    private void renameList(String list) {
        final ListsEntry listsEntry = new ListsEntry(listId, list);
        DatabaseHelper.renameList(listsEntry);
        dismiss();
    }
}
