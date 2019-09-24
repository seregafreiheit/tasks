package ru.frei.tasks;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.frei.tasks.data.Contract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DATA_LOADER = 0;
    TaskCursorAdapter mAdapter;
    ListView listView;

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


        listView = findViewById(R.id.list);
        mAdapter = new TaskCursorAdapter(this, null);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(clickListener);

        getLoaderManager().initLoader(DATA_LOADER, null, this);
    }

    AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = mAdapter.getCursor();
            Uri currentUri = ContentUris.withAppendedId(Contract.Entry.CONTENT_URI, id);
            ContentValues values = new ContentValues();

            int columnDone = cursor.getColumnIndex(Contract.Entry.COLUMN_DONE);
            int colDone = cursor.getInt(columnDone);
            if (colDone == 0) {
                values.put(Contract.Entry.COLUMN_DONE, 1);
                getContentResolver().update(currentUri, values, null, null);
            } else if (colDone == 1) {
                values.put(Contract.Entry.COLUMN_DONE, 0);
                getContentResolver().update(currentUri, values, null, null);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.deleteAll):
                getContentResolver().delete(Contract.Entry.CONTENT_URI, null, null);
                return true;
            case (R.id.deleteDone):
                getContentResolver().delete(Contract.Entry.CONTENT_URI, Contract.Entry.COLUMN_DONE + "= ?",
                        new String[]{"1"});
                return true;
        }
        return super.onOptionsItemSelected(item);
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

        Uri newUri = getContentResolver().insert(Contract.Entry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
        }// else {
         //   Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
       // }
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
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
