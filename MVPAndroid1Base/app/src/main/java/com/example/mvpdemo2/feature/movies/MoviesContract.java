package com.example.mvpdemo2.feature.movies;

import com.example.mvpdemo2.models.GetMoviesResponse;

import java.util.List;

import retrofit2.Response;

public interface MoviesContract {
    interface View {
        void showError(String error);
        void setDataToRecycleView(List<GetMoviesResponse.ResultsBean> results);
        void showLoadingIndicator();
        void hideLoadingIndicator();
    }

    interface Presenter {
        void getMovies(int page);
    }
    interface Model{
        interface OnFinishGetMovies{
            void OnResponse(Response response);
            void OnFailure(String error);
        }

        void getMovies(OnFinishGetMovies onFinishGetMovies, int page);
    }
}
