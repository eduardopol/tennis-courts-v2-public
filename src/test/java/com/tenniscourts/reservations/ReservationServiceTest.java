package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.BusinessException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.tenniscourts.TennisCourt;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void shouldCreateReservation() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).build();

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        CreateReservationRequestDTO request = CreateReservationRequestDTO.builder().guestId(1L).scheduleId(1L).build();

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.saveAndFlush(any(Reservation.class))).thenReturn(reservation);

        ScheduleService scheduleService = mock(ScheduleService.class);
        when(scheduleService.findScheduleForReservation(any())).thenReturn(schedule);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        ReservationDTO reservationDTO = service.bookReservation(request);

        Assert.assertEquals(new Long(1), reservationDTO.getId());
        Assert.assertEquals("Court 1", reservationDTO.getSchedule().getTennisCourt().getName());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreateReservationAlreadyMade() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).build();

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        CreateReservationRequestDTO request = CreateReservationRequestDTO.builder().guestId(1L).scheduleId(1L).build();

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findBySchedule_IdAndReservationStatus(any(), any())).thenReturn(Optional.of(reservation));

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        service.bookReservation(request);

    }

    @Test
    public void shouldFindReservationById() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).build();

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        ReservationDTO reservationDTO = service.findReservation(1L);

        Assert.assertEquals(new Long(1), reservationDTO.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotFindReservationById() {
        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        service.findReservation(1L);
    }

    @Test
    public void shouldCancelReservation() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(LocalDateTime.now().plusDays(1)).build();

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(Reservation.builder().reservationStatus(ReservationStatus.CANCELLED).build());

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        ReservationDTO reservationDTO = service.cancelReservation(1L);

        Assert.assertEquals("CANCELLED", reservationDTO.getReservationStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCancelReservationAlreadyCancelled() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(LocalDateTime.now().plusDays(1)).build();

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.CANCELLED).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        service.cancelReservation(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCancelReservationPastDate() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(LocalDateTime.now().minusDays(1)).build();

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        service.cancelReservation(1L);
    }

    @Test
    public void shouldRescheduleReservation() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(LocalDateTime.now().plusDays(1)).build();
        schedule.setId(1L);

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(Reservation.builder().reservationStatus(ReservationStatus.CANCELLED).schedule(schedule).guest(guest).build());
        when(reservationRepository.saveAndFlush(any())).thenReturn(Reservation.builder().reservationStatus(ReservationStatus.READY_TO_PLAY).build());

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        ReservationDTO reservationDTO = service.rescheduleReservation(1L, 2L);

        Assert.assertEquals("READY_TO_PLAY", reservationDTO.getReservationStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotRescheduleReservationSameSchedule() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        TennisCourt tennisCourt = TennisCourt.builder().name("Court 1").build();
        tennisCourt.setId(1L);
        Schedule schedule = Schedule.builder().tennisCourt(tennisCourt).startDateTime(LocalDateTime.now().plusDays(1)).build();
        schedule.setId(1L);

        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).guest(guest).schedule(schedule).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(Reservation.builder().reservationStatus(ReservationStatus.CANCELLED).schedule(schedule).guest(guest).build());

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        ReservationDTO reservationDTO = service.rescheduleReservation(1L, 1L);

        Assert.assertEquals("READY_TO_PLAY", reservationDTO.getReservationStatus());
    }

    @Test
    public void shouldFinishReservation() {
        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).reservationStatus(ReservationStatus.READY_TO_PLAY).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(Reservation.builder().reservationStatus(ReservationStatus.FINISHED).build());

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        ReservationDTO reservationDTO = service.finishReservation(1L);

        Assert.assertEquals("FINISHED", reservationDTO.getReservationStatus());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFinishReservationInvalidStatus() {
        Reservation reservation = Reservation.builder().value(BigDecimal.TEN).reservationStatus(ReservationStatus.FINISHED).build();
        reservation.setId(1L);

        ReservationMapperImpl reservationMapper = new ReservationMapperImpl();

        ReservationRepository reservationRepository = mock(ReservationRepository.class);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        ScheduleService scheduleService = mock(ScheduleService.class);

        ReservationService service = new ReservationService(reservationRepository, reservationMapper, scheduleService);

        service.finishReservation(1L);
    }

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void getRefundValue75PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(15);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal("7.50"));
    }

    @Test
    public void getRefundValue50PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusHours(8);

        schedule.setStartDateTime(startDateTime);
        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal("5.0"));
    }

    @Test
    public void getRefundValue25PercentRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusMinutes(100);

        schedule.setStartDateTime(startDateTime);
        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal("2.50"));
    }

    @Test
    public void getZeroRefundValue() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now();

        schedule.setStartDateTime(startDateTime);
        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal("0"));
    }

}