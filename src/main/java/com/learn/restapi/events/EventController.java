package com.learn.restapi.events;


import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Controller
@RequestMapping(value="/api",produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;
    public EventController(EventRepo eventRepo, ModelMapper modelMapper) {
        this.eventRepo = eventRepo;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        //ModelMapper
        Event event = modelMapper.map(eventDto,Event.class);
        event.setId(100);
        eventRepo.save(event);
        URI createUri = linkTo(EventController.class).slash("{id}").toUri();
        ResponseEntity result = ResponseEntity.created(createUri).body(event);
        return result;
    }
}
