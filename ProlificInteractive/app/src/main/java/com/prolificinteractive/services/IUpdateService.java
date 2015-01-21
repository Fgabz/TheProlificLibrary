package com.prolificinteractive.services;

import com.prolificinteractive.models.BaseResponse;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedString;

/**
 * Created by Fanilo on 21/01/2015.
 */
public interface IUpdateService
{
    @Multipart
    @PUT("/{url}")
    public void updateBook(
            @Path("url") String url,
            @Part("lastCheckedOutBy") String lastCheckedOutBy,
            Callback<BaseResponse> callback);
}
