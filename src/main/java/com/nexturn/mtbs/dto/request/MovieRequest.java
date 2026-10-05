package com.nexturn.mtbs.dto.request;

import com.nexturn.mtbs.enums.MovieStatus;

public class MovieRequest {

    private MovieStatus status;

    public MovieRequest() {
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }
}