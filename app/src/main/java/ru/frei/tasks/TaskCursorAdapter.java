package ru.frei.tasks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.frei.tasks.data.Contract;

public class TaskCursorAdapter extends CursorAdapter {

    public TaskCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView task = view.findViewById(R.id.taskView);
        ImageView imDone = view.findViewById(R.id.imDone);
        int columnTask = cursor.getColumnIndex(Contract.Entry.COLUMN_TASK);
        int columnDone = cursor.getColumnIndex(Contract.Entry.COLUMN_DONE);
        String taskString = cursor.getString(columnTask);
        int doneString = cursor.getInt(columnDone);
        task.setText(taskString);
        if (doneString == 1) {
            imDone.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            imDone.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
    }
}
