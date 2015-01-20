package com.prolificinteractive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.prolificinteractive.R;
import com.prolificinteractive.models.BookResponse;
import com.prolificinteractive.viewholders.BookHolder;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Fanilo on 19/01/2015.
 */
public class BookAdapter extends ArrayAdapter<BookResponse>
{
    private final Context mContext;

    private final LayoutInflater mInflater;



    public BookAdapter(final Context context){

        super(context, R.layout.book_adapter, new ArrayList<BookResponse>());
        this.mContext = context;
        mInflater= LayoutInflater.from(context);


    }

    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        BookHolder mBookHoler;

        if (convertView == null)
        {
            view = mInflater.inflate(R.layout.book_adapter, parent, false);
            mBookHoler = new BookHolder();

            ButterKnife.inject(mBookHoler, view);
            view.setTag(mBookHoler);
        }
        else{
            view = convertView;
            mBookHoler = (BookHolder) view.getTag();
        }


        setViews(position, mBookHoler);

        return view;
    }

    public void setViews(int position, BookHolder holder){
            holder.getBookTitle().setText(this.getItem(position).getTitle());
            holder.getAuthorName().setText(this.getItem(position).getAuthor());
    }


}