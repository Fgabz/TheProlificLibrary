package com.prolificinteractive.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.gc.materialdesign.views.ButtonFloat;
import com.prolificinteractive.R;
import com.prolificinteractive.adapters.BookAdapter;
import com.prolificinteractive.models.BaseResponse;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.services.IBookService;
import com.prolificinteractive.services.IDeleteBookService;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookFragment extends Fragment
{

    @InjectView(R.id.listView) SwipeMenuListView mListView;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.buttonFloat) ButtonFloat mFloatButton;
    private BookAdapter mAdapter;
    private final IBookService bookService;
    private final IDeleteBookService deleteBookService;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance()
    {
        BookFragment fragment = new BookFragment();


        return fragment;
    }

    public BookFragment()
    {
        bookService = new RestAdapter.Builder()
                .setEndpoint("http://prolific-interview.herokuapp.com/54bd7b72778b4a0008876e08")
                .build()
                .create(IBookService.class);

        deleteBookService = new RestAdapter.Builder()
                .setEndpoint("http://prolific-interview.herokuapp.com/54bd7b72778b4a0008876e08")
                .build()
                .create(IDeleteBookService.class);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @OnClick(R.id.buttonFloat)
    public void addBook(){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up,
                        R.anim.slide_out_up)
                .replace(R.id.container, AddBookFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void createSwipeList(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon


                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);
    }

    private void showListContent(){
        bookService.getBooksLibrary(new Callback<List<BookResponse>>()
        {
            @Override public void success(List<BookResponse> bookResponses, Response response)
            {

                mAdapter.clear();
                mAdapter.addAll(bookResponses);
                mAdapter.notifyDataSetChanged();
            }

            @Override public void failure(RetrofitError error)
            {

            }
        });
    }


    public void deleteMethod(int position, int index) {
        if (index == 0){
            BookResponse book = (BookResponse) mListView.getItemAtPosition(position);
            mAdapter.remove(book);
            mAdapter.notifyDataSetChanged();
            deleteBookService.deleteBook(book.getUrl().substring(1), new Callback<BaseResponse>() {
                @Override public void success(BaseResponse baseResponse, Response response)
                {
                    Toast.makeText(getActivity(), "Book Deleted", Toast.LENGTH_SHORT).show();
                }

                @Override public void failure(RetrofitError error)
                {
                    Toast.makeText(getActivity(), "Error with deleted ;(", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mAdapter = new BookAdapter(getActivity());
        this.showListContent();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.inject(this, view);
        mListView.setAdapter(mAdapter);
        this.createSwipeList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh()
            {
                showListContent();
            }
        });
        mSwipeRefreshLayout.animate().cancel();
        mSwipeRefreshLayout.setRefreshing(false);


        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index) {
                    case 0:
                        deleteMethod(position,index);
                }

                return false;
            }
        });

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
