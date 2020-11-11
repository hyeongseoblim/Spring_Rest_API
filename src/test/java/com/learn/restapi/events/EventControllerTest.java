package com.learn.restapi.events;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {

    @Autowired // 스프링 mvc test 핵심 클래스. 웹서버 사용 x, 스프링 mvc가 요청하는 과정 확인 가능.
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepo eventRepo;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder().
                name("spring").description("spring rest api description")
                .beginEventDateTime(LocalDateTime.of(2020,11,12,4,22))
                .endEventDateTime(LocalDateTime.of(2020,11,12,8,30))
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,22,12,0))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,22,19,0))
                .basePrice(100)
                .maxPrice(200)
                .location("GangNam")
                .build();
        event.setId(10);
        Mockito.when(eventRepo.save(event)).thenReturn(event);

        mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }
}
