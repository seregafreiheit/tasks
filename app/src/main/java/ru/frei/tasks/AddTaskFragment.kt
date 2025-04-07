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

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.TasksEntry;
import ru.frei.tasks.databinding.FragmentTaskAddListDialogBinding;

public class AddTaskFragment extends BottomSheetDialogFragment {

    private long listID;
    private long taskID;
    private boolean isUpdate;

    private FragmentTaskAddListDialogBinding binding;
    private EditText editText;
    private AppDatabase database;

    private TasksEntry tasksEntryForUpdate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_add_list_dialog, container, false);

        listID = getArguments().getLong("listId");
        isUpdate = getArguments().getBoolean("isUpdate");
        taskID = getArguments().getLong("taskId", 0);

        editText = binding.addEditText;
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        database = AppDatabase.getInstance(getContext());
        if (isUpdate) {
            getTask();
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String task = editText.getText().toString();
                    if (task.equals("")) {
                        dismiss();
                        return false;
                    }
                    if (isUpdate) {
                        updateTask(task);
                    } else {
                        addTask(task);
                    }
                }
                return true;
            }
        });
    }

    private void addTask(String task) {
        final TasksEntry tasksEntry = new TasksEntry(task);
        tasksEntry.setList_id(listID);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.tasksDao().insertTask(tasksEntry);
            }
        });
        dismiss();
    }

    private void updateTask(String task) {
        final TasksEntry tasksEntry = new TasksEntry(taskID, task);
        tasksEntry.setList_id(listID);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.tasksDao().updateTask(tasksEntry);
            }
        });
        dismiss();
    }

    private void getTask() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                tasksEntryForUpdate = database.tasksDao().loadTaskById(taskID);
                editText.setText(tasksEntryForUpdate.getTask());
            }
        });
    }
}
