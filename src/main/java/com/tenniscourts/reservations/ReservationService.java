package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.BusinessException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    private static final BigDecimal RESERVATION_VALUE = BigDecimal.TEN;

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ScheduleService scheduleService;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        Optional<Reservation> existingReservation = reservationRepository.findBySchedule_IdAndReservationStatus(createReservationRequestDTO.getScheduleId(),
                                                                                                                ReservationStatus.READY_TO_PLAY);
        if (existingReservation.isPresent()) {
            throw  new BusinessException("Schedule already reserved.");
        }

        Schedule scheduleForReservation = scheduleService.findScheduleForReservation(createReservationRequestDTO.getScheduleId());
        Reservation reservation = reservationMapper.map(createReservationRequestDTO);

        reservation.setSchedule(scheduleForReservation);
        reservation.setValue(RESERVATION_VALUE);

        return reservationMapper.map(reservationRepository.saveAndFlush(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> new EntityNotFoundException("Reservation not found."));
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    public ReservationDTO finishReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Reservation not found."));

        if (!reservation.getReservationStatus().equals(ReservationStatus.READY_TO_PLAY)) {
            throw new BusinessException("Invalid status to finish game");
        }

        return reservationMapper.map(this.updateReservation(reservation, reservation.getValue(), ReservationStatus.FINISHED));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> new EntityNotFoundException("Reservation not found."));
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        } else if ( hours < 24 && hours >= 12) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.75));
        } else if ( hours < 12 && hours >= 2) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.5));
        } else if ( hours < 2 && minutes >= 1) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.5));
        }

        return BigDecimal.ZERO;
    }

    @Transactional
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }
}
