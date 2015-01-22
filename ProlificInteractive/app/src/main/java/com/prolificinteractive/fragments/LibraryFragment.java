package com.prolificinteractive.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.SnackBar;
import com.prolificinteractive.R;
import com.prolificinteractive.adapters.BookAdapter;
import com.prolificinteractive.models.BaseResponse;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.services.IDeleteBookService;
import com.prolificinteractive.services.IDeleteLibraryService;
import com.prolificinteractive.services.ILibrarykService;
import com.prolificinteractive.utils.Globals;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LibraryFragment extends Fragment
{

    @InjectView(R.id.listView) SwipeMenuListView mListView;
    @InjectView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.buttonFloat) ButtonFloat mFloatButton;
    private BookAdapter mAdapter;
    private final ILibrarykService librarykService;
    private final IDeleteBookService deleteBookService;
    private final IDeleteLibraryService mDeleteLibraryService;

    private RecyclerView.LayoutManager mLayoutManager;


    public static LibraryFragment newInstance()
    {
        LibraryFragment fragment = new LibraryFragment();


        return fragment;
    }

    public LibraryFragment()
    {
        Globals g = Globals.getInstance();
        librarykService = new RestAdapter.Builder()
                .setEndpoint(g.getUrl())
                .build()
                .create(ILibrarykService.class);

        deleteBookService = new RestAdapter.Builder()
                .setEndpoint(g.getUrl())
                .build()
                .create(IDeleteBookService.class);

        mDeleteLibraryService = new RestAdapter.Builder()
                .setEndpoint(g.getUrl())
                .build()
                .create(IDeleteLibraryService.class);
    }

    /*
    Setting the dimension of the circle
     */
    private int dp2px(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /*
        Function for adding a book
    */
    @OnClick(R.id.buttonFloat)
    public void addBook()
    {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up,
                        R.anim.slide_in_up, R.anim.slide_out_up)
                .replace(R.id.container, AddBookFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    /*
        Function for creating the custom the submenu of the ListView
    */
    private void createSwipeList()
    {
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(180));
                // set a icon


                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);
    }

    @Override public void onResume()
    {
        super.onResume();
        showListContent();
    }

    /*Function for loading all the library in the ListView*/
    private void showListContent()
    {
        librarykService.getBooksLibrary(new Callback<List<BookResponse>>()
        {
            @Override public void success(List<BookResponse> bookResponses, Response response)
            {

                mAdapter.clear();
                mAdapter.addAll(bookResponses);
                mAdapter.notifyDataSetChanged();
            }

            @Override public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), "Error while downloading", Toast.LENGTH_SHORT).show();
            }
        });

        if (mSwipeRefreshLayout != null)
        {
            new Handler().postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    // stop progress animation
                    mSwipeRefreshLayout.animate().cancel();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);

        }

    }


    /*
        Function for deleting a book
    */
    public void deleteBook(int position, int index)
    {
        if (index == 0)
        {
            BookResponse book = (BookResponse) mListView.getItemAtPosition(position);
            mAdapter.remove(book);
            mAdapter.notifyDataSetChanged();
            deleteBookService.deleteBook(book.getUrl().substring(1), new Callback<BaseResponse>()
            {
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
        setHasOptionsMenu(true);
        mAdapter = new BookAdapter(getActivity());
        this.showListContent();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.inject(this, view);
        mListView.setAdapter(mAdapter);
        this.createSwipeList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override public void onRefresh()
            {
                showListContent();
            }

        });


        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        deleteBook(position, index);
                }

                return false;
            }
        });

        return view;
    }


    @OnItemClick(R.id.listView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        BookResponse book = (BookResponse) mListView.getItemAtPosition(position);

        getFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.container, BookFragment.newInstance(book.getUrl().substring(1),
                        book.getAuthor()))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
        Function for deleting the all library
    */
    public void deleteLibrary()
    {
        SnackBar snackbar = new SnackBar(getActivity(),
                "Are you sure that you want to delete all your library ?",
                "YES", new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                mDeleteLibraryService.deleteLibrary(new Callback<BaseResponse>()
                {
                    @Override public void success(BaseResponse baseResponse, Response response)
                    {
                        Toast.makeText(getActivity(), "Library Deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void failure(RetrofitError error)
                    {
                        Toast.makeText(getActivity(), " Error Library Deleted :(",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        snackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete)
        {
            deleteLibrary();

            //Refreshing the listView
            showListContent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
