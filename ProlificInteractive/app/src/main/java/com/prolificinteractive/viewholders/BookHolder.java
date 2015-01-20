package com.prolificinteractive.viewholders;

import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.R;

import butterknife.InjectView;
import lombok.Data;

/**
 * Created by Fanilo on 20/01/2015.
 */

@Data
public class BookHolder
{
    @InjectView(R.id.booktitle)TextView bookTitle;
    @InjectView(R.id.bookauthor)TextView authorName;
    @InjectView(R.id.image_view)ImageView imageView;



}
