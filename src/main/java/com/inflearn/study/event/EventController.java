package com.inflearn.study.event;


import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value ="/api",produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private static  EventRepository eventRepository;

    @PostMapping("/events")
    public ResponseEntity createEvent(@RequestBody Event event){
        URI createURI = linkTo(methodOn(EventController.class).
                createEvent(event)).
                slash(event.getId()).
                toUri();


        return ResponseEntity.created(createURI).build();
    }

}
