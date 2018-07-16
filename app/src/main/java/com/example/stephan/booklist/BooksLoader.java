package com.example.stephan.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Bhavya Arora on 1/10/2018.
 */

public class BooksLoader extends AsyncTaskLoader<List<Book>> {
    String url;
    public static List<Book> arrayList = null;

    public BooksLoader(Context context, String url) {
        super(context);
        if(url == null){
            return;
        }
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if(url == null) {
            return null;
        }
        arrayList = QueryUtils.fetchBooksData(url);
        return arrayList;
    }
}
