package ru.frei.tasks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Provider extends ContentProvider {
    private static final int TASKS = 100;
    private static final int TASK_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_TASKS, TASKS);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_TASKS + "/#", TASK_ID);
    }
    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                cursor = database.query(Contract.Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TASK_ID:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.Entry.TABLE_NAME,projection, selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query unknown uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final  int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return Contract.Entry.CONTENT_LIST_TYPE;
            case TASK_ID:
                return Contract.Entry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException( "Unknown uri" + uri + "with match"+ match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return insertTasks (uri, values);
                default:
                    throw new IllegalArgumentException("is not supported "+ uri);
        }
    }

    private Uri insertTasks (Uri uri, ContentValues values){
        String task = values.getAsString(Contract.Entry.COLUMN_TASK);
        if (task == null) {
            throw new IllegalArgumentException("requares task");
        }
        Boolean done = values.getAsBoolean(Contract.Entry.COLUMN_DONE);
        if (done == null){
            throw new IllegalArgumentException("done hz");
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(Contract.Entry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match =sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                rowsDeleted = database.delete(Contract.Entry.TABLE_NAME, selection,selectionArgs);
                break;
            case TASK_ID:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("deletet is not supported" + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASKS:
                return updateTasks (uri, values, selection, selectionArgs);
            case TASK_ID:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateTasks (uri, values, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("update is not supported for " + uri);
        }
    }

    private int updateTasks (Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(Contract.Entry.COLUMN_TASK)) {
            String task = values.getAsString(Contract.Entry.COLUMN_TASK);
        }
        if (values.containsKey(Contract.Entry.COLUMN_DONE)) {
            int done = values.getAsInteger(Contract.Entry.COLUMN_DONE);
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdate = database.update(Contract.Entry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdate != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdate;
    }
}
