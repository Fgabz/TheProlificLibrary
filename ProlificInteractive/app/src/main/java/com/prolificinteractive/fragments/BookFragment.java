package com.prolificinteractive.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.gc.materialdesign.views.ButtonRectangle;
import com.prolificinteractive.R;
import com.prolificinteractive.models.BaseResponse;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.services.IBookService;
import com.prolificinteractive.services.IUpdateService;
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
    private final IUpdateService updateService;
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

        this.updateService = new RestAdapter.Builder()
                .setEndpoint(g.getUrl())
                .build()
                .create(IUpdateService.class);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_share:
                shareBook();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    /*
    Function Sharing the current book to the other application
*/
    public void shareBook()
    {
        Intent intent = null;

        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(Intent.EXTRA_TEXT, " I'm reading this current book \" " +
                mBookTitle.getText().toString() + " \" of " + mAuthorBook.getText().toString());
        Intent i = new Intent(Intent.createChooser(intent, "Share Book to..."));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getActivity().startActivity(i);
    }

    private void setCircleColor(String author)
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(author);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(author.substring(0, 1).toString(), color);
        mImageBook.setImageDrawable(drawable);

    }


    /*
    Function showing the dialog for the checkout of the book
    */
    @OnClick(R.id.checkout_button)
    public void checkoutAction()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_fragment);
        dialog.setTitle("Checkout");

        TextView txt = (TextView) dialog.findViewById(R.id.dialogTitle);
        txt.setText("Under which username you want to checkout ?");

        final EditText edit = (EditText) dialog.findViewById(R.id.editdialog_name);

        ButtonRectangle cancelButton = (ButtonRectangle) dialog.findViewById(R.id.cancel_button);
        ButtonRectangle checkoutButton = (ButtonRectangle)
                dialog.findViewById(R.id.checkoutcalidate_button);

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });


        checkoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (edit.getText().toString() != null && !edit.getText().toString().equals(""))
                    updtateBook(edit.getText().toString(), dialog);

            }
        });


        dialog.show();
    }

    /*
        Function calling the service fot updating the book
    */
    public void updtateBook(String name, final Dialog dialog)
    {
        updateService.updateBook(mBookUrl, name, new Callback<BaseResponse>()
        {
            @Override public void success(BaseResponse baseResponse, Response response)
            {
                Toast.makeText(getActivity(), "Checkout done ! :)", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                getFragmentManager().popBackStackImmediate();
            }

            @Override public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), "Checkout error ! :(", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
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
