package com.inflearn.study.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflearn.study.common.RestDocsConfiguration;
import com.inflearn.study.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;// 객체를 JSON으로 변환하기 위한

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
    @TestDescription("잘못된 입력값을 사용한 경우에 에러가 발생하는 테스트")
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
    @TestDescription("입력값을 EvnetDto로 생성했을때 이벤트로 생성하는 테스트")
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-event").description("link to query-event"),
                                linkWithRel("update-event").description("link to update-event"),
                                linkWithRel("profile").description("this api profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time begin of new event enrollment"),
                                fieldWithPath("endEnrollmentDateTime").description("date time end of new event enrollment"),
                                fieldWithPath("beginEventDateTime").description("start time of new Event"),
                                fieldWithPath("endEventDateTime").description("end time of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit enrollment of new Event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("response header location"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("response header content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("Id of new Event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time begin of new event enrollment"),
                                fieldWithPath("endEnrollmentDateTime").description("date time end of new event enrollment"),
                                fieldWithPath("beginEventDateTime").description("start time of new Event"),
                                fieldWithPath("endEventDateTime").description("end time of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit enrollment of new Event"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline or not"),
                                fieldWithPath("eventStatus").description("eventStatus of new Event")
                        )
                ));

    }

    @Test
    @TestDescription("입력값이 비어있을 때 400에러를 반환하는 테스트트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("잘못된 입력값이 들어 왔을 때 400에러를 반환하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("test Event Dto")
                .description("Testing number 1")
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.of(2021, 2, 1, 8, 0))
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회 하기")
    public void queryEvent() throws Exception {
        //given
        IntStream.range(0, 30).forEach(this::generateEvent);

        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    private void generateEvent(int index) {
        Event event = Event.builder()
                .name("Event" + index)
                .description("test Event")
                .build();

        this.eventRepository.save(event);
    }
}