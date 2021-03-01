package com.modern.android.commons.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface ViewHolderFactory<T extends ListItem> {
    ViewHolder<? extends T> create(LayoutInflater inflater, ViewGroup parent, int viewType);
}
