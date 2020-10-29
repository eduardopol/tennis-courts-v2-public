package com.tenniscourts.guests;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TennisCourtApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GuestControllerIT {

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
    public void test01NewGuest() throws Exception {
        postGuest(createGuest("Eduardo Poletto"));
    }

    @Test
    public void test02FindById() throws Exception {
        Long guestId = 3L;
        MvcResult result = getGuest(guestId);

        GuestDTO response = mapper.readValue(result.getResponse().getContentAsString(), GuestDTO.class);


        assertEquals(guestId, response.getId());
        assertEquals("Eduardo Poletto", response.getName());
    }

    @Test
    public void test03NotFoundById() throws Exception {
        getGuestNotFound(4L);
    }

    @Test
    public void test04UpdateGuest() throws Exception {
        putGuest(3L, updateGuest("Eduardo Sartori Poletto"));
    }

    @Test
    public void test05FindByName() throws Exception {
        Long guestId = 3L;
        MvcResult result = getGuestByName("Eduardo Sartori Poletto");

        GuestDTO response = mapper.readValue(result.getResponse().getContentAsString(), GuestDTO.class);


        assertEquals(guestId, response.getId());
        assertEquals("Eduardo Sartori Poletto", response.getName());
    }

    @Test
    public void test06FindAll() throws Exception {
        MvcResult result = getAllGuests();

        List<GuestDTO> response = Arrays.asList(mapper.readValue(result.getResponse().getContentAsString(), GuestDTO[].class));

        assertEquals(3, response.size());
        assertEquals("Eduardo Sartori Poletto", response.get(2).getName());
    }

    @Test
    public void test07DeleteGuest() throws Exception {
        deleteGuest(3L);
    }

    private String createGuest(String guestName) throws Exception {
        CreateGuestRequestDTO request = new CreateGuestRequestDTO();
        request.setName(guestName);
        String jsonPost = mapper.writeValueAsString(request);

        return jsonPost;
    }

    private String updateGuest(String guestName) throws Exception {
        UpdateGuestRequestDTO request = new UpdateGuestRequestDTO();
        request.setName(guestName);
        String jsonPost = mapper.writeValueAsString(request);

        return jsonPost;
    }

    private MvcResult postGuest(String jsonPost) throws Exception {
        return mockMvc
                .perform(post("/guests")
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*")
                        .content(jsonPost))
                .andExpect(status().isCreated()).andReturn();
    }

    private MvcResult putGuest(Long guestId, String jsonPost) throws Exception {
        return mockMvc
                .perform(put("/guests/{0}", guestId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*")
                        .content(jsonPost))
                .andExpect(status().isNoContent()).andReturn();
    }

    private MvcResult deleteGuest(Long guestId) throws Exception {
        return mockMvc
                .perform(delete("/guests/{0}", guestId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isNoContent()).andReturn();
    }

    private MvcResult getAllGuests() throws Exception {
        return mockMvc
                .perform(get("/guests/all")
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

    private MvcResult getGuest(Long guestId) throws Exception {
        return mockMvc
                .perform(get("/guests/{0}", guestId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isOk()).andReturn();
    }

    private MvcResult getGuestNotFound(Long guestId) throws Exception {
        return mockMvc
                .perform(get("/guests/{0}", guestId)
                        .header("Content-Type", "application/json")
                        .header("Access-Control-Allow-Origin", "*"))
                .andExpect(status().isNotFound()).andReturn();
    }

    private MvcResult getGuestByName(String guestName) throws Exception {
        MockHttpServletRequestBuilder getBuilder = get("/guests").param("guestName", guestName);

        MvcResult result = mockMvc.perform(getBuilder.header("Content-Type", "application/json")
                .header("Access-Control-Allow-Origin", "*")).andExpect(status().isOk()).andReturn();
        return result;
    }

}
