package ru.frei.tasks;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;

import java.util.ArrayList;
import java.util.List;

import ru.frei.tasks.data.Contract;

public class TaskAdapter extends RecyclerViewCursorAdapter<TaskAdapter.ViewHolder>  {

    private  final List<String> mItems = new ArrayList<>();

    public TaskAdapter (Context context){
        super(context);
        setupCursorAdapter(null,0, R.layout.list_item, false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        setViewHolder(holder);
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());

    }


    public  class ViewHolder extends RecyclerViewCursorViewHolder{
        private TextView tw;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.taskView);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            tw.setText(cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_TASK)));
            mItems.add(cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_TASK)));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.Entry._ID));
            itemView.setTag(id);
        }
    }

    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        String prev = mItems.remove(fromPosition);
        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }
}
