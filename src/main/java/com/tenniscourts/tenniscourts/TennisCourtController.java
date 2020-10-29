package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.tenniscourts.api.TennisCourtControllerApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RestController
@RequestMapping(value = TennisCourtController.URL_BASE)
public class TennisCourtController extends BaseRestController implements TennisCourtControllerApi {

    public static final String URL_BASE = "/tennis-courts";

    private final TennisCourtService tennisCourtService;

    @Override
    @PostMapping
    public ResponseEntity<Void> addTennisCourt(@Valid @RequestBody TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @Override
    @GetMapping("/{tennisCourtId}")
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable @NotNull Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtDTOById(tennisCourtId));
    }

    @Override
    @GetMapping("/{tennisCourtId}/with-schedule")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable @NotNull Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
