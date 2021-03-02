package com.inflearn.study.event;

import com.inflearn.study.common.ErrorsResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getMaxPrice() < eventDto.getBasePrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "basePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
            errors.reject("wrongPrices", "Values to prices are Wrong ");
        }
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getEndEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }
    }

}
