package com.hfc.drag;
import android.os.ParcelFileDescriptor;
import com.hfc.drag.IDragDropListener;

interface IDragDrop {
    int getShareIntentSize(String pkg, String type);
    oneway void saveBitmap(in ParcelFileDescriptor pfd, in IDragDropListener listener);
}