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


@Controller
@RequestMapping(value="/api",produces = MediaTypes.HAL_JSON_VALUE) //클래스의 내의 응답은 HAL_JSON 형식으로 보내게 된다.
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        //ModelMapper
        Event event = modelMapper.map(eventDto,Event.class); //Dto를 기존의 entity에 맞게 옮겨주는 역할을 함.는
        // eventDto를 Event class 맞는 인스턴스로 바꿔달라.
        event.setId(10);
        eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(event.getId()).toUri(); //
        ResponseEntity result = ResponseEntity.created(createUri).body(event);
        return result;
    }
}
