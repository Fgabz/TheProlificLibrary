package com.prolificinteractive.services;

import com.prolificinteractive.models.AddBookResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Fanilo on 20/01/2015.
 */
public interface IAddBookService
{
    @FormUrlEncoded
    @POST("/books/")
    public void addBook(
            @Field("author") String author,
            @Field("categories") String categories,
            @Field("title") String title,
            @Field("publisher") String publisher,
            Callback<AddBookResponse> callback);

}
