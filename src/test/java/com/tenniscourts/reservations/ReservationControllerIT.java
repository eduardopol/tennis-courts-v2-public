package com.tenniscourts.reservations;

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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TennisCourtApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReservationControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test01NewReservation() throws Exception {
        postReservation(createReservationRequest(1L, 1L));
    }

    @Test
    public void test02FindById() throws Exception {
        Long reservationId = 1L;
        MvcResult result = getReservation(reservationId);

        ReservationDTO response = mapper.readValue(result.getResponse().getContentAsString(), ReservationDTO.class);

        assertEquals(reservationId, response.getId());
        assertEquals("READY_TO_PLAY", response.getReservationStatus());
    }

    @Test
    public void test03CancelReservation() throws Exception {
        postCancelReservation(1L);
    }

    @Test
    public void test04NewReservation() throws Exception {
        postReservation(createReservationRequest(1L, 1L));
    }

    @Test
    public void test05RescheduleReservation() throws Exception {
        postRescheduleReservation(2L, 2L);
    }

    @Test
    public void test06FinishReservation() throws Exception {
        postFinishReservation(3L);
    }

    private String createReservationRequest(Long guestId, Long scheduleId) throws Exception {
        CreateReservationRequestDTO request = new CreateReservationRequestDTO();
        request.setGuestId(guestId);
        request.setScheduleId(scheduleId);
        String jsonPost = mapper.writeValueAsString(request);

        return jsonPost;
    }

    private MvcResult postReservation(String jsonPost) throws Exception {
        return mockMvc
                .perform(post("/tennis-courts/reservations")
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*")
                        .content(jsonPost))
                .andExpect(status().isCreated()).andReturn();
    }

    private MvcResult postCancelReservation(Long reservationId) throws Exception {
        return mockMvc
                .perform(post("/tennis-courts/reservations/{0}/cancel", reservationId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

    private MvcResult postFinishReservation(Long reservationId) throws Exception {
        return mockMvc
                .perform(post("/tennis-courts/reservations/{0}/finish", reservationId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

    private MvcResult postRescheduleReservation(Long reservationId, Long scheduleId) throws Exception {
        return mockMvc
                .perform(put("/tennis-courts/reservations/{0}/reschedule/{1}", reservationId, scheduleId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

    private MvcResult getReservation(Long reservationId) throws Exception {
        return mockMvc
                .perform(get("/tennis-courts/reservations/{0}", reservationId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

}
