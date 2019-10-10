package ru.frei.tasks;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.frei.tasks.data.Contract;
import ru.frei.tasks.data.DBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DATA_LOADER = 0;
    private static SQLiteDatabase mDatabase;
    static TaskAdapter adapter;
    RecyclerView rw;
     static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

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
        adapter = new TaskAdapter(this);
        rw.setHasFixedSize(true);
        rw.setLayoutManager(new LinearLayoutManager(this));
        rw.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rw);

        getLoaderManager().initLoader(DATA_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                getContentResolver().delete(Contract.Entry.CONTENT_URI, null, null);
        return super.onOptionsItemSelected(item);
    }

    public static void deleteTask(long id){
        mDatabase.delete(Contract.Entry.TABLE_NAME, Contract.Entry._ID + "=" + id, null);
        adapter.swapCursor(getAllItems());
    }

    private static Cursor getAllItems() {
        return mDatabase.query(
                Contract.Entry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void saveTask() {
        EditText editText = findViewById(R.id.editText);
        String task = editText.getText().toString().trim();
        if (task.equals("")) {
            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.COLUMN_TASK, task);
        values.put(Contract.Entry.COLUMN_DONE, 0);

        getContentResolver().insert(Contract.Entry.CONTENT_URI, values);

        editText.setText(null);

        // код ниже для того чтобы скрывать клавиатуру после добавления задачи
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(add.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @NonNull
    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                Contract.Entry._ID,
                Contract.Entry.COLUMN_TASK,
                Contract.Entry.COLUMN_DONE
        };
        return new CursorLoader(this, Contract.Entry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
