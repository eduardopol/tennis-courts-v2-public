package com.tenniscourts.tenniscourts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.TennisCourtApplication;
import com.tenniscourts.guests.GuestDTO;
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
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TennisCourtApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TennisCourtControllerIT {

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
    public void test01NewTennisCourt() throws Exception {
        postTennisCourt(createTennisCourt("Court 1"));
    }

    @Test
    public void test02FindById() throws Exception {
        Long tennisCourtId = 3L;
        MvcResult result = getTennisCourt(tennisCourtId);

        GuestDTO response = mapper.readValue(result.getResponse().getContentAsString(), GuestDTO.class);


        assertEquals(tennisCourtId, response.getId());
        assertEquals("Court 1", response.getName());
    }

    @Test
    public void test03FindByIdWithSchedule() throws Exception {
        Long tennisCourtId = 1L;
        MvcResult result = getTennisCourtWithSchedule(tennisCourtId);

        TennisCourtDTO response = mapper.readValue(result.getResponse().getContentAsString(), TennisCourtDTO.class);


        assertEquals(tennisCourtId, response.getId());
        assertNotNull(response.getTennisCourtSchedules());
    }

    private String createTennisCourt(String guestName) throws Exception {
        TennisCourtDTO request = new TennisCourtDTO();
        request.setName(guestName);
        String jsonPost = mapper.writeValueAsString(request);

        return jsonPost;
    }

    private MvcResult postTennisCourt(String jsonPost) throws Exception {
        return mockMvc
                .perform(post("/tennis-courts")
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*")
                        .content(jsonPost))
                .andExpect(status().isCreated()).andReturn();
    }

    private MvcResult getTennisCourt(Long tennisCourtId) throws Exception {
        return mockMvc
                .perform(get("/tennis-courts/{0}", tennisCourtId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

    private MvcResult getTennisCourtWithSchedule(Long tennisCourtId) throws Exception {
        return mockMvc
                .perform(get("/tennis-courts/{0}/with-schedule", tennisCourtId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

}
