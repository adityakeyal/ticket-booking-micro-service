package com.sapient.theater.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 *
 * Java Record to send Show Information. It is an immutable representation of a show
 *
 */
public record ShowInformation(String theaterName, String screenName, @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime showTime) {

}
