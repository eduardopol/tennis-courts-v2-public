package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final TennisCourtService tennisCourtService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper,
                           @Lazy TennisCourtService tennisCourtService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
        this.tennisCourtService = tennisCourtService;
    }

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourt tennisCourt = tennisCourtService.findTennisCourtById(createScheduleRequestDTO.getTennisCourtId());

        Schedule schedule = scheduleMapper.map(createScheduleRequestDTO);
        schedule.setTennisCourt(tennisCourt);
        schedule.setEndDateTime(schedule.getStartDateTime().plusHours(1));

        return scheduleMapper.map(scheduleRepository.saveAndFlush(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByStartDateTimeGreaterThanEqualAndStartDateTimeLessThanEqual(startDate, endDate)
                .stream().map(scheduleMapper::map).collect(Collectors.toList());
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() ->
                new EntityNotFoundException("Schedule not found.")
        );
    }

    public Schedule findScheduleForReservation(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found."));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
