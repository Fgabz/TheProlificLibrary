package com.prolificinteractive.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.prolificinteractive.R;
import com.prolificinteractive.models.AddBookResponse;
import com.prolificinteractive.services.IAddBookService;
import com.prolificinteractive.utils.Globals;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddBookFragment extends Fragment
{

    private final IAddBookService mBookService;
    @InjectView(R.id.edit_booktitle) EditText mBookTitle;
    @InjectView(R.id.edit_author) EditText mAuthor;
    @InjectView(R.id.edit_publisher) EditText mPublisher;
    @InjectView(R.id.edit_categories) EditText mCategories;
    @InjectView(R.id.submit_button) ButtonRectangle mSubmitButton;


    public static AddBookFragment newInstance()
    {
        AddBookFragment fragment = new AddBookFragment();
        return fragment;
    }

    public AddBookFragment()
    {
        Globals g = Globals.getInstance();
        mBookService = new RestAdapter.Builder()
                .setEndpoint(g.getUrl())
                .build()
                .create(IAddBookService.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    //Function for adding a book int the library
    @OnClick(R.id.submit_button)
    public void submit()
    {
        if (!mAuthor.getText().toString().equals("") && !mBookTitle.getText().toString().equals(""))
        {
            mBookService.addBook(mAuthor.getText().toString(), mCategories.getText().toString(),
                    mBookTitle.getText().toString(),
                    mPublisher.getText().toString(), new Callback<AddBookResponse>()
                    {
                        @Override
                        public void success(AddBookResponse addBookResponse, Response response)
                        {
                                Toast.makeText(getActivity(), "Book Added !",
                                        Toast.LENGTH_SHORT).show();

                            getFragmentManager().popBackStackImmediate();
                        }

                        @Override public void failure(RetrofitError error)
                        {
                            Toast.makeText(getActivity(), "An error happened ! :( ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);
        ButterKnife.inject(this, view);

        mAuthor.setText("");
        mBookTitle.setText("");
        mCategories.setText("");
        mPublisher.setText("");

        return view;
    }



    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();

    }


}
