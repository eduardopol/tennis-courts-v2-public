package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class GuestServiceTest {

    @Test
    public void shouldCreateGuest() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        CreateGuestRequestDTO request = new CreateGuestRequestDTO();
        request.setName("Rafael Nadal");

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.saveAndFlush(any(Guest.class))).thenReturn(guest);

        GuestService service = new GuestService(guestRepository, guestMapper);

        GuestDTO guestDTO = service.addGuest(request);

        assertEquals(new Long(1), guestDTO.getId());
        assertEquals("Rafael Nadal", guestDTO.getName());
    }

    @Test
    public void shouldUpdateGuest() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        Guest guestUpdated = Guest.builder().name("Rafael Nadal Parera").build();
        guestUpdated.setId(1L);

        UpdateGuestRequestDTO request = new UpdateGuestRequestDTO();
        request.setName("Rafael Nadal Parera");

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.saveAndFlush(any(Guest.class))).thenReturn(guestUpdated);
        when(guestRepository.findById(any())).thenReturn(Optional.of(guest));

        GuestService service = new GuestService(guestRepository, guestMapper);

        GuestDTO guestDTO = service.updateGuest(1L, request);

        assertEquals(new Long(1), guestDTO.getId());
        assertEquals("Rafael Nadal Parera", guestDTO.getName());
    }

    @Test
    public void shouldDeleteGuest() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findById(any())).thenReturn(Optional.of(guest));

        GuestService service = new GuestService(guestRepository, guestMapper);

        service.deleteGuest(1L);
    }

    @Test
    public void shouldFindGuestById() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findById(any())).thenReturn(Optional.of(guest));

        GuestService service = new GuestService(guestRepository, guestMapper);

        Guest guestById = service.findGuestById(1L);

        assertEquals(new Long(1), guestById.getId());
        assertEquals("Rafael Nadal", guestById.getName());
    }

    @Test
    public void shouldFindGuestDTOById() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findById(any())).thenReturn(Optional.of(guest));

        GuestService service = new GuestService(guestRepository, guestMapper);

        GuestDTO guestDTO = service.findGuestDTOById(1L);

        assertEquals(new Long(1), guestDTO.getId());
        assertEquals("Rafael Nadal", guestDTO.getName());
    }

    @Test
    public void shouldFindByName() {
        Guest guest = Guest.builder().name("Rafael Nadal").build();
        guest.setId(1L);

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findByName(any())).thenReturn(Optional.of(guest));

        GuestService service = new GuestService(guestRepository, guestMapper);

        GuestDTO guestDTO = service.findGuestByName("Rafael Nadal");

        assertEquals(new Long(1), guestDTO.getId());
        assertEquals("Rafael Nadal", guestDTO.getName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowErrorEntityNotFoundWhenFindGuestById() {
        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findById(any())).thenReturn(Optional.empty());

        GuestService service = new GuestService(guestRepository, guestMapper);

        service.findGuestById(1L);

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowErrorEntityNotFoundWhenFindGuestDTOById() {
        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findById(any())).thenReturn(Optional.empty());

        GuestService service = new GuestService(guestRepository, guestMapper);

        service.findGuestDTOById(1L);

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowErrorEntityNotFoundWhenFindByName() {
        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findByName(any())).thenReturn(Optional.empty());

        GuestService service = new GuestService(guestRepository, guestMapper);

        service.findGuestByName("Rafael Nadal");

    }

    @Test
    public void shouldFindAllGuests() {
        Guest guest1 = Guest.builder().name("Rafael Nadal").build();
        guest1.setId(1L);

        Guest guest2 = Guest.builder().name("Roger Federer").build();
        guest2.setId(2L);

        GuestMapperImpl guestMapper = new GuestMapperImpl();

        GuestRepository guestRepository = mock(GuestRepository.class);
        when(guestRepository.findAll()).thenReturn(Arrays.asList(guest1, guest2));

        GuestService service = new GuestService(guestRepository, guestMapper);

        List<GuestDTO> guestsDTO = service.findAllGuests();

        assertEquals(2, guestsDTO.size());
        assertEquals(new Long(1), guestsDTO.get(0).getId());
        assertEquals("Rafael Nadal", guestsDTO.get(0).getName());
    }

}
