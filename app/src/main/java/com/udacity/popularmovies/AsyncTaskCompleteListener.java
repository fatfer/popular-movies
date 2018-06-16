package com.udacity.popularmovies;


interface AsyncTaskCompleteListener<T>
{
    void onTaskComplete(T result);
}
