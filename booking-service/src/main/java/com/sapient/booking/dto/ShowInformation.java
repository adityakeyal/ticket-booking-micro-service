package com.sapient.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 *
 * Java Record with the Show Information. It is an immutable representation of a show
 *
 */
public record ShowInformation(String theatreOwner, String theatreName, String screenName, @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime showTime) {

}

