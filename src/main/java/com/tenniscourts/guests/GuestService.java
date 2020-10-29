package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuestService {

    private static final BigDecimal RESERVATION_VALUE = BigDecimal.TEN;

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDTO addGuest(CreateGuestRequestDTO createGuestRequestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(createGuestRequestDTO)));
    }

    public GuestDTO updateGuest(Long guestId, UpdateGuestRequestDTO updateGuestRequestDTO) {
        Guest guest = findGuestById(guestId);

        guest.setName(updateGuestRequestDTO.getName());

        return guestMapper.map(guestRepository.saveAndFlush(guest));
    }

    public void deleteGuest(Long guestId) {
        Guest guest = findGuestById(guestId);

        guestRepository.delete(guest);
    }

    public Guest findGuestById(Long guestId) {
        return guestRepository.findById(guestId).orElseThrow(() -> new EntityNotFoundException("Guest not found."));
    }

    public GuestDTO findGuestDTOById(Long guestId) {
        return guestRepository.findById(guestId).map(guestMapper::map).orElseThrow(() ->
                new EntityNotFoundException("Guest not found.")
        );
    }

    public GuestDTO findGuestByName(String name) {
        return guestRepository.findByName(name).map(guestMapper::map).orElseThrow(() ->
                new EntityNotFoundException("Guest not found.")
        );
    }

    public List<GuestDTO> findAllGuests() {
        return guestRepository.findAll().stream().map(guestMapper::map).collect(Collectors.toList());
    }

}
