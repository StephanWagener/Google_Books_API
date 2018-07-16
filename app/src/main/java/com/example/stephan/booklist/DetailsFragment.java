package com.example.stephan.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class DetailsFragment extends Fragment {

    public DetailsFragment () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.details_fragment, container, false);

        final Book book = ((BooksActivity)getActivity()).getClickedListItem();

        TextView title = view.findViewById(R.id.details_fragment_title);
        title.setText(book.getTitle());

        TextView author = view.findViewById(R.id.details_fragment_author);
        author.setText(book.getAuthor());

        TextView publisher = view.findViewById(R.id.details_fragment_publisher);
        publisher.setText(book.getPublisher());

        ImageView image = view.findViewById(R.id.details_fragment_image);
        new DownloadImageTask(image).execute(book.getImageUrl());

        Button show = view.findViewById(R.id.show_book_button);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BooksActivity)getActivity()).openWebsite(book.getInfoUrl());
            }
        });

        Button infoAuthor = view.findViewById(R.id.show_author_button);
        infoAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BooksActivity)getActivity()).startGoogleSearch(book.getAuthor());
            }
        });

        Button infoPublisher = view.findViewById(R.id.show_publisher_button);
        infoPublisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BooksActivity)getActivity()).startGoogleSearch(book.getPublisher());
            }
        });

        return view;
    }
}
