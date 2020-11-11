package com.learn.restapi.events;


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

    public EventController(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        event.setId(10);
        eventRepo.save(event);
        URI createUri = linkTo(methodOn(EventController.class).createEvent(event)).toUri();
        ResponseEntity result = ResponseEntity.created(createUri).body(event);
        return result;
    }
}
