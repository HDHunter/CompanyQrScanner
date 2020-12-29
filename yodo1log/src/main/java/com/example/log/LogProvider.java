package com.example.log;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LogProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        LogUtils.e("onCreate:  " + this);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LogUtils.e("query:  " + this);
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        LogUtils.e("getType:  " + this);
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        LogUtils.e("insert:  " + this);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        LogUtils.e("delete:  " + this);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        LogUtils.e("update:  " + this);
        return 0;
    }
}
