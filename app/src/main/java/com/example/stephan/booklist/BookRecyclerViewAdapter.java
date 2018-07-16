package com.example.stephan.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookRecyclerViewAdapter extends Adapter<BookRecyclerViewAdapter.ListItemViewHolder>{

    private final BooksActivity activity;
    private ArrayList<Book> items;

    public BookRecyclerViewAdapter(ArrayList<Book> items, BooksActivity activity)
    {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        if (position == (this.items.size()-1))
        {
            this.activity.setButtonVisibility(true);
        }

        Book listItem = this.items.get(position);
        holder.title.setText(listItem.getTitle());

        holder.author.setText(listItem.getAuthor());

        new DownloadImageTask(holder.image).execute(listItem.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView image;
        TextView author;

        ListItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.list_item_title);
            author = itemView.findViewById(R.id.list_item_author);
            image = itemView.findViewById(R.id.list_item_cover);
        }
    }
}
