package ru.frei.tasks;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.frei.tasks.data.AppDatabase;
import ru.frei.tasks.data.TasksEntry;

public class MainActivity extends AppCompatActivity {


    private TasksAdapter adapter;
    private RecyclerView rw;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveTask();
                    handled = true;
                }
                return handled;
            }
        });

        rw = findViewById(R.id.rw);
        adapter = new TasksAdapter(this);
        rw.setHasFixedSize(true);
        rw.setLayoutManager(new LinearLayoutManager(this));
        rw.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<TasksEntry> tasks = adapter.getTasks();
                        database.tasksDao().deleteTask(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(rw);

        database = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<TasksEntry>>() {
            @Override
            public void onChanged(@Nullable List<TasksEntry> tasksEntries) {
                adapter.setTasks(tasksEntries);
            }
        });
    }

    private void saveTask() {
        EditText editText = findViewById(R.id.editText);
        String task = editText.getText().toString();
        if (task.equals("")) {
            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
            return;
        }


        final TasksEntry tasksEntry = new TasksEntry(task);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.tasksDao().insertTask(tasksEntry);
            }
        });

        editText.setText(null);

        // код ниже для того чтобы скрывать клавиатуру после добавления задачи
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(add.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
