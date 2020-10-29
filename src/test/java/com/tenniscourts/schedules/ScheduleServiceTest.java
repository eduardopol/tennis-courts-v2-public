package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ScheduleServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void shouldCreateSchedule() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        LocalDateTime startDate = LocalDateTime.now().plusDays(2);

        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate).endDateTime(startDate.plusHours(1)).build();
        schedule.setId(1L);

        CreateScheduleRequestDTO request = CreateScheduleRequestDTO.builder().tennisCourtId(1L).startDateTime(startDate).build();

        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.saveAndFlush(any(Schedule.class))).thenReturn(schedule);

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);
        when(tennisCourtService.findTennisCourtById(any())).thenReturn(tennisCourt);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        ScheduleDTO scheduleDTO = service.addSchedule(request);

        Assert.assertEquals(new Long(1), scheduleDTO.getId());
        Assert.assertEquals(startDate, scheduleDTO.getStartDateTime());
        Assert.assertEquals(startDate.plusHours(1), scheduleDTO.getEndDateTime());

    }

    @Test
    public void shouldFindScheduleById() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        LocalDateTime startDate = LocalDateTime.now().plusDays(2);

        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate).endDateTime(startDate.plusHours(1)).build();
        schedule.setId(1L);

        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        ScheduleDTO scheduleDTO = service.findSchedule(1L);

        Assert.assertEquals(new Long(1), scheduleDTO.getId());
        Assert.assertEquals(startDate, scheduleDTO.getStartDateTime());
        Assert.assertEquals(startDate.plusHours(1), scheduleDTO.getEndDateTime());

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotFindScheduleById() {
        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findById(any())).thenReturn(Optional.empty());

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        service.findSchedule(1L);
    }

    @Test
    public void shouldFindScheduleForReservation() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        LocalDateTime startDate = LocalDateTime.now().plusDays(2);

        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate).endDateTime(startDate.plusHours(1)).build();
        schedule.setId(1L);

        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findById(any())).thenReturn(Optional.of(schedule));

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        Schedule scheduleForReservation = service.findScheduleForReservation(1L);

        Assert.assertEquals(new Long(1), scheduleForReservation.getId());
        Assert.assertEquals(startDate, scheduleForReservation.getStartDateTime());
        Assert.assertEquals(startDate.plusHours(1), scheduleForReservation.getEndDateTime());

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotFindScheduleForReservation() {
        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findById(any())).thenReturn(Optional.empty());

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        service.findScheduleForReservation(1L);
    }

    @Test
    public void shouldFindScheduleByDates() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        LocalDateTime startDate = LocalDateTime.now().plusDays(2);

        Schedule schedule1 = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate).endDateTime(startDate.plusHours(1)).build();
        schedule1.setId(1L);

        Schedule schedule2 = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate.plusHours(1)).endDateTime(startDate.plusHours(2)).build();
        schedule2.setId(2L);

        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findByStartDateTimeGreaterThanEqualAndStartDateTimeLessThanEqual(any(), any())).thenReturn(Arrays.asList(schedule1, schedule2));

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        List<ScheduleDTO> schedules = service.findSchedulesByDates(LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        Assert.assertEquals(2, schedules.size());
        Assert.assertEquals(new Long(1), schedules.get(0).getId());
        Assert.assertEquals(startDate, schedules.get(0).getStartDateTime());
        Assert.assertEquals(startDate.plusHours(1), schedules.get(0).getEndDateTime());

    }

    @Test
    public void shouldFindScheduleByTennisCourtId() {
        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);

        LocalDateTime startDate = LocalDateTime.now().plusDays(2);

        Schedule schedule1 = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate).endDateTime(startDate.plusHours(1)).build();
        schedule1.setId(1L);

        Schedule schedule2 = Schedule.builder().tennisCourt(tennisCourt).startDateTime(startDate.plusHours(1)).endDateTime(startDate.plusHours(2)).build();
        schedule2.setId(2L);

        ScheduleMapperImpl scheduleMapper = new ScheduleMapperImpl();

        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
        when(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(any())).thenReturn(Arrays.asList(schedule1, schedule2));

        TennisCourtService tennisCourtService = mock(TennisCourtService.class);

        ScheduleService service = new ScheduleService(scheduleRepository, scheduleMapper, tennisCourtService);

        List<ScheduleDTO> schedules = service.findSchedulesByTennisCourtId(1L);

        Assert.assertEquals(2, schedules.size());
        Assert.assertEquals(new Long(1), schedules.get(0).getId());
        Assert.assertEquals(startDate, schedules.get(0).getStartDateTime());
        Assert.assertEquals(startDate.plusHours(1), schedules.get(0).getEndDateTime());

    }

}