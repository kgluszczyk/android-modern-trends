package com.modern.android.commons.list;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder<T extends ListItem> extends RecyclerView.ViewHolder {

    protected Context context;

    public ViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
    }

    public abstract void bind(T item);
}
