package com.prolificinteractive.services;

import com.prolificinteractive.models.BookResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Fanilo on 20/01/2015.
 */
public interface IBookService
{
    @GET("/{url}")
    public void getSpecificBook(
            @Path("url") String url,
            Callback<BookResponse> callback);
}
