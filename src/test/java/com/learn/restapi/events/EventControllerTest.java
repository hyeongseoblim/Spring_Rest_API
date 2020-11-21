package com.learn.restapi.events;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired // 스프링 mvc test 핵심 클래스. 웹서버 사용 x, 스프링 mvc가 요청하는 과정 확인 가능.
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepo eventRepo;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring").description("spring rest api description")
                .beginEventDateTime(LocalDateTime.of(2020,11,12,4,22))
                .endEventDateTime(LocalDateTime.of(2020,11,12,8,30))
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,22,12,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,22,19,0))
                .basePrice(100)
                .maxPrice(200)
                .location("GangNam")
                .build();
        Mockito.when(eventRepo.save(event)).thenReturn(event);

        mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE));
    }
    @Test
    public void createBadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring").description("spring rest api description")
                .beginEventDateTime(LocalDateTime.of(2020,11,12,4,22))
                .endEventDateTime(LocalDateTime.of(2020,11,12,8,30))
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,22,12,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,22,19,0))
                .basePrice(100)
                .maxPrice(200)
                .location("GangNam")
                .free(true)
                .offline(false)
                .build();
        Mockito.when(eventRepo.save(event)).thenReturn(event);

        mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
