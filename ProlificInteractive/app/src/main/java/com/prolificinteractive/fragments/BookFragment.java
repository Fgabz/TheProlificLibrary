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
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;
import com.prolificinteractive.R;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.services.IBookService;
import com.prolificinteractive.utils.Globals;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookFragment extends Fragment
{
    private static final String ARG_URL = "url";
    private static final String ARG_AUTHOR = "author";

    private final IBookService bookService;
    private String mBookUrl;
    private String mAuthor;


    @InjectView(R.id.view_titlebook) TextView mBookTitle;
    @InjectView(R.id.view_author) TextView mAuthorBook;
    @InjectView(R.id.tag_name) TextView mTagName;
    @InjectView(R.id.lao_name) TextView mLaoName;
    @InjectView(R.id.publisher_name) TextView mPublisher;
    @InjectView(R.id.imageviewBook) ImageView mImageBook;
    @InjectView(R.id.checkout_button) ButtonRectangle mCheckoutButton;

    public static BookFragment newInstance(String url, String author)
    {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_AUTHOR, author);
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
        {
            mBookUrl = getArguments().getString(ARG_URL);
            mAuthor = getArguments().getString(ARG_AUTHOR);
        }


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

    private void setCircleColor(String author)
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(author);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(author.substring(0, 1).toString(), color);
        mImageBook.setImageDrawable(drawable);

    }


    @OnClick(R.id.checkout_button)
    public void checkoutAction()
    {
        Dialog dialog = new Dialog(getActivity(),
                "Checkout Book", "Under which name you want to checkout ?");

//        final EditText input = new EditText(getActivity());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);
//
//        dialog.setContentView(input);

        // Set accept click listenner
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            {

            }
        });

        // Set cancel click listenner
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            {

            }
        });

        dialog.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.inject(this, view);

        setCircleColor(mAuthor);

        return view;
    }


}
