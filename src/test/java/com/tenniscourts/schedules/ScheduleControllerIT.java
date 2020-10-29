package com.tenniscourts.schedules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.TennisCourtApplication;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TennisCourtApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper;

    private LocalDateTime startDate;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        startDate = LocalDateTime.now().plusHours(12);

    }

    @Test
    public void test01NewSchedule() throws Exception {
        postSchedule(createScheduleRequest(1L, startDate));
    }

    @Test
    public void test02FindById() throws Exception {
        Long scheduleId = 3L;
        MvcResult result = getSchedule(scheduleId);

        ScheduleDTO response = mapper.readValue(result.getResponse().getContentAsString(), ScheduleDTO.class);

        assertEquals(scheduleId, response.getId());
    }

    private String createScheduleRequest(Long tennisCourtId, LocalDateTime startDateTime) throws Exception {
        CreateScheduleRequestDTO request = CreateScheduleRequestDTO.builder().tennisCourtId(tennisCourtId).startDateTime(startDateTime).build();

        String jsonPost = mapper.writeValueAsString(request);

        return jsonPost;
    }

    private MvcResult postSchedule(String jsonPost) throws Exception {
        return mockMvc
                .perform(post("/tennis-courts/schedules")
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*")
                        .content(jsonPost))
                .andExpect(status().isCreated()).andReturn();
    }

    private MvcResult getSchedule(Long scheduleId) throws Exception {
        return mockMvc
                .perform(get("/tennis-courts/schedules/{0}", scheduleId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

}
