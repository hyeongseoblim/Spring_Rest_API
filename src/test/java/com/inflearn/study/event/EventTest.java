package com.inflearn.study.event;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder().name("Inflearn test RESTAPI").description("Testing").build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        String name ="Event";
        String description = "Spring";

        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}