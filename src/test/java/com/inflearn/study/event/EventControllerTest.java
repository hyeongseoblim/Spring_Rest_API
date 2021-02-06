package com.inflearn.study.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;// 객체를 JSON으로 변환하기 위한

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder().name("Spring")
                .description("REST API DEVELOPMENT WITH SPRING")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 1, 15, 2, 11))
                .endEnrollmentDateTime(LocalDateTime.of(2021, 1, 16, 2, 11))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 20, 0, 0))
                .endEventDateTime(LocalDateTime.of(2021, 1, 22, 0, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("능곡")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(Matchers.not(true)));
    }

    @Test
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder().name("Spring")
                .id(200)
                .description("REST API DEVELOPMENT WITH SPRING")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 1, 15, 2, 11))
                .endEnrollmentDateTime(LocalDateTime.of(2021, 1, 16, 2, 11))
                .beginEventDateTime(LocalDateTime.of(2021, 1, 20, 0, 0))
                .endEventDateTime(LocalDateTime.of(2021, 1, 22, 0, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .eventStatus(EventStatus.PUBLISHED)
                .location("능곡")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createEvent_EvnetDto() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("test Event Dto")
                .description("Testing number 1")
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.of(2021, 2, 11, 8, 0))
                .beginEnrollmentDateTime(LocalDateTime.now())
                .endEnrollmentDateTime(LocalDateTime.of(2021, 2, 9, 18, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(50)
                .location("GangNam")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

}