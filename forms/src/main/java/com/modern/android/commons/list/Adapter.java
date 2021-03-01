package com.modern.android.commons.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class Adapter<T extends ListItem> extends RecyclerView.Adapter<ViewHolder> {

    protected List<T> data;

    private ViewHolderFactory factory;

    public Adapter(ViewHolderFactory factory) {
        this();
        this.factory = factory;
    }

    public Adapter() {
        data = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getViewHolderType();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return factory.create(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    public void setData(@NonNull List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setFactory(ViewHolderFactory factory) {
        this.factory = factory;
    }
}
