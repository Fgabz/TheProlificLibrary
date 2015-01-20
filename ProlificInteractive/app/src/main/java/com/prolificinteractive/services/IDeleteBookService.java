package com.prolificinteractive.services;

import com.prolificinteractive.models.BaseResponse;


import retrofit.Callback;

import retrofit.http.DELETE;
import retrofit.http.Path;

/**
 * Created by Fanilo on 20/01/2015.
 */
public interface IDeleteBookService
{
    @DELETE("/{url}")
    public void deleteBook(
            @Path("url") String url,
            Callback<BaseResponse> callback);

}
