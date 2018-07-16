package com.example.stephan.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    private static final String bookFetchUrl = "https://www.googleapis.com/books/v1/volumes";
    private static final String TAG = "stwagene";
    private Book clickedItem;
    private RecyclerView mRecyclerView;
    private ArrayList<Book> bookList = new ArrayList<>();
    private BookRecyclerViewAdapter recyclerAdapter;
    private EditText search_entry;
    private static final int BOOKS_LOADER_ID = 1;
    private View downloadButton;
    private int startIndexURL = 0;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Checking the Network State
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            Toast.makeText(this, "No Internet!", Toast.LENGTH_SHORT).show();
            this.finish();
            System.exit(0);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite("https://github.com/StephanWagener");
            }
        });

        // Initialize the RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        setUpRecyclerView();

        // Initialize a recycler adapter and set it on the recycler view
        recyclerAdapter = new BookRecyclerViewAdapter(bookList, this);
        mRecyclerView.setAdapter(recyclerAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AppBarLayout appBarLayout = findViewById(R.id.app_bar);
                appBarLayout.setExpanded(false);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.activity_layout, new DetailsFragment(), DetailsFragment.class.getSimpleName());
                ft.addToBackStack(DetailsFragment.class.getSimpleName());
                ft.commit();
                clickedItem = bookList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        downloadButton = findViewById(R.id.download_more_books_button);
        downloadButton.setVisibility(View.INVISIBLE);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startIndexURL >= 40)
                {
                    Toast.makeText(getApplicationContext(), "Limit erreicht!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    downloadMoreBooks();
                }
                setButtonVisibility(false);
            }
        });
    }

    public void setButtonVisibility(boolean setVisible)
    {
        if (setVisible)
        {
            downloadButton.setVisibility(View.VISIBLE);
        }
        else
        {
            downloadButton.setVisibility(View.INVISIBLE);
        }

    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public Book getClickedListItem()
    {
        return clickedItem;
    }

    public void openWebsite(String website)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(website));
        startActivity(intent);
    }

    private void downloadMoreBooks()
    {
        startIndexURL += 10;
        getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, this);
        getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);
        Log.i(TAG, "downloadMoreBooks: "  + bookList);
    }

    public void searchButton(View view){
        startIndexURL = 0;
        query = "";
        bookList.clear();
        recyclerAdapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(BOOKS_LOADER_ID, null, this);
        getLoaderManager().initLoader(BOOKS_LOADER_ID, null, this);
        Log.i(TAG, "searchButton: "  + bookList);
    }

    public void startGoogleSearch(String text)
    {
        String escapedQuery = null;
        try {
            escapedQuery = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            this.finish();
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        if (startIndexURL == 0)
        {
            search_entry = findViewById(R.id.search_entry);
            query = search_entry.getText().toString();
            if(query.isEmpty() || query.length() == 0){
                search_entry.setError("Please Enter Any Book");
                return new BooksLoader(this, null);
            }
        }

        //WITH URI
        Uri baseUri = Uri.parse(bookFetchUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", query)
                  .appendQueryParameter("maxResults", "10")
                  .appendQueryParameter("startIndex", ""+startIndexURL);

        //when we click om searchButton keyboard will hide
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        search_entry.setText("");

        //Returning a new Loader Object
        return new BooksLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if(books !=null && !books.isEmpty()){
            prepareBooks(books);
            Log.i(TAG, "onLoadFinished: ");
        }
    }

    private void prepareBooks(List<Book> booksList) {
        bookList.addAll(booksList);
        Log.i(TAG, "prepareBooks: " + bookList);

        //notifiying the recycleradapter that data has been changed
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i(QueryUtils.TAG, "onLoaderReset: ");
        if(recyclerAdapter == null){
            return;
        }
        bookList.clear();
        recyclerAdapter.notifyDataSetChanged();
        Log.i(TAG, "onLoaderReset: " + bookList);
    }
}
