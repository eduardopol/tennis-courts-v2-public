package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.guests.api.GuestControllerApi;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = GuestController.URL_BASE)
public class GuestController extends BaseRestController implements GuestControllerApi {

    public static final String URL_BASE = "/guests";

    private final GuestService guestService;

    @Override
    @PostMapping
    public ResponseEntity<Void> addGuest(@Valid @RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(createGuestRequestDTO).getId())).build();
    }

    @Override
    @PutMapping("/{guestId}")
    public ResponseEntity<Void> updateGuest(@PathVariable @NotNull Long guestId,
                                            @Valid @RequestBody UpdateGuestRequestDTO updateGuestRequestDTO) {
        guestService.updateGuest(guestId, updateGuestRequestDTO);

        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(@PathVariable @NotNull Long guestId) {
        guestService.deleteGuest(guestId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestDTO> findGuest(@PathVariable @NotNull Long guestId) {
        return ResponseEntity.ok(guestService.findGuestDTOById(guestId));
    }

    @Override
    @GetMapping
    public ResponseEntity<GuestDTO> findGuestByName(@ApiParam(value = "Guest name", example = "Roger Federer")  @RequestParam(name = "guestName", required = true)  String guestName) {
        return ResponseEntity.ok(guestService.findGuestByName(guestName));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }

}
