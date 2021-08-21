package com.example.mvpdemo2.feature.movies;

import com.example.mvpdemo2.models.GetMoviesResponse;
import retrofit2.Response;

public class MoviesPresenter implements MoviesContract.Presenter, MoviesContract.Model.OnFinishGetMovies{

    MoviesContract.View view;
    MoviesContract.Model model;
    public MoviesPresenter(MoviesContract.View view, MoviesContract.Model model){
        this.view=view;
        this.model=model;
    }

    @Override
    public void getMovies(int page) {
        view.showLoadingIndicator();

    }

    @Override
    public void OnResponse(Response response) {
        if (response.code() == 200) {
            view.setDataToRecycleView(((GetMoviesResponse)response.body()).getResults());
            //loi vi chua dung response --> ep kieu hoac Response<...>
        }
        view.hideLoadingIndicator();
    }

    @Override
    public void OnFailure(String error) {
        view.showError(error);
        view.hideLoadingIndicator();
    }
}
