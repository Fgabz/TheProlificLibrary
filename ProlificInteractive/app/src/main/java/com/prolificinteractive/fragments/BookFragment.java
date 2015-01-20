package com.prolificinteractive.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.prolificinteractive.R;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.services.IBookService;
import com.prolificinteractive.utils.Globals;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookFragment extends Fragment
{
    private static final String ARG_URL = "url";
    private final IBookService bookService;
    private String mBookUrl;

    @InjectView(R.id.view_titlebook) TextView mBookTitle;
    @InjectView(R.id.view_author) TextView mAuthorBook;
    @InjectView(R.id.tag_name) TextView mTagName;
    @InjectView(R.id.lao_name) TextView mLaoName;
    @InjectView(R.id.publisher_name) TextView mPublisher;
    @InjectView(R.id.imageviewBook) ImageView mImageBook;

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


        bookService.getSpecificBook(mBookUrl, new Callback<BookResponse>()
        {
            @Override public void success(BookResponse bookResponse, Response response)
            {
                if (bookResponse.getTitle() != null)
                    mBookTitle.setText(bookResponse.getTitle().toString());
                if (bookResponse.getAuthor() != null)
                    mAuthorBook.setText(bookResponse.getAuthor().toString());
                if (bookResponse.getCategories() != null)
                    mTagName.setText(bookResponse.getCategories().toString());
                if (bookResponse.getLastCheckedOut() != null ||
                        bookResponse.getLastCheckedOutBy() != null)
                mLaoName.setText(bookResponse.getLastCheckedOutBy() + " @ " +
                        bookResponse.getLastCheckedOut());
                if (bookResponse.getPublisher() != null)
                    mPublisher.setText(bookResponse.getPublisher().toString());
            }

            @Override public void failure(RetrofitError error)
            {

            }
        });
    }

    private void setCircleColor()
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        String author = mAuthorBook.getText().toString();
        int color = generator.getColor(author);


        TextDrawable drawable = TextDrawable.builder()
                .buildRound(author.substring(0, 1).toString(), color);
        mImageBook.setImageDrawable(drawable);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.inject(this, view);
        this.setCircleColor();
        return view;
    }


}
