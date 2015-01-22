package com.prolificinteractive.services;

import com.prolificinteractive.models.BaseResponse;

import retrofit.Callback;
import retrofit.http.DELETE;

/**
 * Created by Fanilo on 22/01/2015.
 */
public interface IDeleteLibraryService
{
    @DELETE("/clean/")
    public void deleteLibrary(
            Callback<BaseResponse> callback);
}
