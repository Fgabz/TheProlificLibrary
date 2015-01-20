package com.prolificinteractive.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.R;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.services.IBookService;
import com.prolificinteractive.utils.Globals;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookFragment extends Fragment
{
    private static final String ARG_URL = "url";
    private final IBookService bookService;
    private String mBookUrl;

    public static BookFragment newInstance(String url)
    {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);

        fragment.setArguments(args);
        return fragment;
    }

    public BookFragment()
    {
        Globals g = Globals.getInstance();
        this.bookService = new RestAdapter.Builder()
            .setEndpoint(g.getUrl())
            .build()
            .create(IBookService.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mBookUrl = getArguments().getString(ARG_URL);



        bookService.getSpecificBook(mBookUrl, new Callback<BookResponse>() {
            @Override public void success(BookResponse bookResponse, Response response)
            {

            }

            @Override public void failure(RetrofitError error)
            {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.inject(this, view);
        return view;
    }





}
