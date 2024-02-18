package com.sapient.booking.service.external;

import com.sapient.booking.dto.ShowInformation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.UUID;

public interface ShowService {

    @GetExchange("/shows/{id}")
    ShowInformation getShow(@PathVariable UUID id);
}
