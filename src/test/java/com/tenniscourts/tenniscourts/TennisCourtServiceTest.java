package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.ScheduleDTO;
import com.tenniscourts.schedules.ScheduleService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TennisCourtServiceTest {

    @Test
    public void shouldCreateTennisCourt() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        TennisCourtDTO request = new TennisCourtDTO();
        request.setName("Court 1");

        TennisCourtMapperImpl tennisCourtMapper = new TennisCourtMapperImpl();

        TennisCourtRepository tennisCourtRepository = mock(TennisCourtRepository.class);
        when(tennisCourtRepository.saveAndFlush(any(TennisCourt.class))).thenReturn(tennisCourt);

        ScheduleService scheduleService = mock(ScheduleService.class);

        TennisCourtService service = new TennisCourtService(tennisCourtRepository, tennisCourtMapper, scheduleService);

        TennisCourtDTO tennisCourtDTO = service.addTennisCourt(request);

        assertEquals(new Long(1), tennisCourtDTO.getId());
        assertEquals("Court 1", tennisCourtDTO.getName());
    }

    @Test
    public void shouldFindTennisCourtById() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        TennisCourtMapperImpl tennisCourtMapper = new TennisCourtMapperImpl();

        TennisCourtRepository tennisCourtRepository = mock(TennisCourtRepository.class);
        when(tennisCourtRepository.findById(any())).thenReturn(Optional.of(tennisCourt));

        ScheduleService scheduleService = mock(ScheduleService.class);

        TennisCourtService service = new TennisCourtService(tennisCourtRepository, tennisCourtMapper, scheduleService);

        TennisCourt tennisCourt1 = service.findTennisCourtById(1L);

        assertEquals(new Long(1), tennisCourt1.getId());
        assertEquals("Court 1", tennisCourt1.getName());
    }

    @Test
    public void shouldFindTennisCourtDTOById() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        TennisCourtMapperImpl tennisCourtMapper = new TennisCourtMapperImpl();

        TennisCourtRepository tennisCourtRepository = mock(TennisCourtRepository.class);
        when(tennisCourtRepository.findById(any())).thenReturn(Optional.of(tennisCourt));

        ScheduleService scheduleService = mock(ScheduleService.class);

        TennisCourtService service = new TennisCourtService(tennisCourtRepository, tennisCourtMapper, scheduleService);

        TennisCourtDTO tennisCourtDTO = service.findTennisCourtDTOById(1L);

        assertEquals(new Long(1), tennisCourtDTO.getId());
        assertEquals("Court 1", tennisCourtDTO.getName());
    }

    @Test
    public void shouldFindTennisCourtWithScheduleById() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        TennisCourtMapperImpl tennisCourtMapper = new TennisCourtMapperImpl();

        TennisCourtRepository tennisCourtRepository = mock(TennisCourtRepository.class);
        when(tennisCourtRepository.findById(any())).thenReturn(Optional.of(tennisCourt));

        ScheduleService scheduleService = mock(ScheduleService.class);
        when(scheduleService.findSchedulesByTennisCourtId(any())).thenReturn(new ArrayList<ScheduleDTO>());

        TennisCourtService service = new TennisCourtService(tennisCourtRepository, tennisCourtMapper, scheduleService);

        TennisCourtDTO tennisCourtDTO = service.findTennisCourtWithSchedulesById(1L);

        assertEquals(new Long(1), tennisCourtDTO.getId());
        assertEquals("Court 1", tennisCourtDTO.getName());
        assertNotNull(tennisCourtDTO.getTennisCourtSchedules());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowErrorEntityNotFoundWhenFindTennisCourtById() {
        TennisCourtMapperImpl tennisCourtMapper = new TennisCourtMapperImpl();

        ScheduleService scheduleService = mock(ScheduleService.class);
        TennisCourtRepository tennisCourtRepository = mock(TennisCourtRepository.class);
        when(tennisCourtRepository.findById(any())).thenReturn(Optional.empty());

        TennisCourtService service = new TennisCourtService(tennisCourtRepository, tennisCourtMapper, scheduleService);

        service.findTennisCourtById(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowErrorEntityNotFoundWhenFindTennisCourtDTOById() {
        TennisCourtMapperImpl tennisCourtMapper = new TennisCourtMapperImpl();

        ScheduleService scheduleService = mock(ScheduleService.class);
        TennisCourtRepository tennisCourtRepository = mock(TennisCourtRepository.class);
        when(tennisCourtRepository.findById(any())).thenReturn(Optional.empty());

        TennisCourtService service = new TennisCourtService(tennisCourtRepository, tennisCourtMapper, scheduleService);

        service.findTennisCourtDTOById(1L);
    }

}
