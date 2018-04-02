package com.udacity.popularmovies;


public interface AsyncTaskCompleteListener<T>
{
    public void onTaskComplete(T result);
}
