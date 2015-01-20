package com.prolificinteractive.services;

import com.prolificinteractive.models.BookResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Fanilo on 19/01/2015.
 */
public interface ILibrarykService
{
    @GET("/books")
    public void getBooksLibrary(
            Callback<List<BookResponse>> callback);
}
