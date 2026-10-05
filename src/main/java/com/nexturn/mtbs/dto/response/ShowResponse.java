package com.nexturn.mtbs.dto.response;

import com.nexturn.mtbs.enums.ShowStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowResponse {

    private Long id;

    private Long movieId;
    private String movieTitle;

    private Long screenId;
    private String screenName;

    private Long theatreId;
    private String theatreName;

    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private BigDecimal ticketPrice;

    private ShowStatus status;

    public ShowResponse() {
    }

    public ShowResponse(
            Long id,
            Long movieId,
            String movieTitle,
            Long screenId,
            String screenName,
            Long theatreId,
            String theatreName,
            LocalDate showDate,
            LocalTime startTime,
            LocalTime endTime,
            BigDecimal ticketPrice,
            ShowStatus status
    ) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.screenId = screenId;
        this.screenName = screenName;
        this.theatreId = theatreId;
        this.theatreName = theatreName;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }
}