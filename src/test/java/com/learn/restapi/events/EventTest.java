package com.learn.restapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {
    @Test
    public void builder(){
        Event event = Event.builder().name("Inflearn REST API").description("rest api development").build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        String name = "Event";
        String hihi = "hihi";
        //set
        event.setName(name);
        event.setDescription(hihi);
        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(hihi);
    }
}