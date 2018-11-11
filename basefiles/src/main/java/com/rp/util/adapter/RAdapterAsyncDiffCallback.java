package com.rp.util.adapter;

import android.util.Log;

import com.rp.util.adapter.annotations.ComparableItem;

import java.lang.reflect.Field;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public final class RAdapterAsyncDiffCallback<T> extends DiffUtil.ItemCallback<T> {

    public static final String TAG = RAdapterAsyncDiffCallback.class.getSimpleName();
    private RAdapterPayloadWatcher payloadWatcher;

    public RAdapterAsyncDiffCallback() {}

    public RAdapterAsyncDiffCallback(RAdapterPayloadWatcher payloadWatcher) {
        this.payloadWatcher = payloadWatcher;
    }

    public void addPayloadWatcher(RAdapterPayloadWatcher payloadWatcher) {
        this.payloadWatcher = payloadWatcher;
    }

    @Override
    public boolean areItemsTheSame(@NonNull T t, @NonNull T t1) {

        Object oldValue = getValue(t);
        Object newValue = getValue(t1);

        Log.e(TAG, "isUnique: old : " + oldValue + " new : " + newValue + " equal : " + Objects.equals(oldValue, newValue));

        return Objects.equals(oldValue, newValue);
    }

    @Override
    public boolean areContentsTheSame(@NonNull T t, @NonNull T t1) {
        return t.equals(t1);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {

        if (payloadWatcher == null)
            return null;

        return payloadWatcher.getPayloadData(oldItem, newItem);
    }

    private Object getValue(Object element) {

        Field[] declaredFields = element.getClass().getDeclaredFields();

        Object data = null;

        for (Field declaredField : declaredFields) {

            boolean isAccessible = declaredField.isAccessible();

            if (!isAccessible)
                declaredField.setAccessible(true);

            if (declaredField.isAnnotationPresent(ComparableItem.class)) {
                try {
                    data = declaredField.get(element);
                    //declaredField.setAccessible(isAccessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }

        }

        return data;
    }
}
