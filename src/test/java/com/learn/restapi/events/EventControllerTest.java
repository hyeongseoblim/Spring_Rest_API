package com.learn.restapi.events;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@RunWith(SpringRunner.class)//테스트를 위한 어노테이션
@SpringBootTest //목킹할게 너무 많으면 슬라이스 할 수 있게 통합테스트를 이용하여 스프링부트테스트 어노테이션 사용.
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired // 스프링 mvc test 핵심 클래스. 웹서버 사용 x, 스프링 mvc가 요청하는 과정 확인 가능.
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

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
                .eventStatus(EventStatus.DRFAT)
                .build();


        mockMvc.perform(post("/api") // 접근할 uri mapping 주소
                .contentType(MediaType.APPLICATION_JSON) // 헤더에 미디어 타입
                .accept(MediaTypes.HAL_JSON) //제이슨 파일이다.
                .content(objectMapper.writeValueAsString(event))) //object maaper를 사용해 이벤트를 스트링으로
                .andDo(print()) // 프린트한다.
                .andExpect(status().isCreated()) // 생성된 상태를
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))//
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100))) // 매칭되는지 안되는지 확인하는 방법 Matchers
                .andExpect(jsonPath("free").value(Matchers.not(true)));
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
                .offline(false)// 받을수 없는 애들을 받을 경우 fail을 반환하게 함.
                .build();


        mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
