package com.prolificinteractive.models;

import lombok.Data;

/**
 * Created by Fanilo on 19/01/2015.
 */

@Data
public class BookResponse
{
    protected String author;
    protected String categories;
    protected String id;
    protected String lastCheckedOut;
    protected String lastCheckedOutBy;
    protected String publisher;
    protected String title;
    protected String url;

}
