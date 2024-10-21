package hr.rba.credit_card_client_service.feature.card_application;

import hr.rba.credit_card_client_service.configuration.logging.LogExecutionTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("card-applications")
@RequiredArgsConstructor
@Tag(name = "Zahtjevi za izdavanjem kartice", description = "API zahtjeva za izdavanjem kartice")
public class CardApplicationController {

  private final CardApplicationResourceService cardApplicationResourceService;

  @GetMapping("/{oib}")
  @Operation(
      summary = "Dohvat po OIB-u",
      description =
          "Vraća objekt zahtjeva za izdavanjem kartice. Ne vraća ništa ukoliko zahtjev nije pronađen.")
  @LogExecutionTime
  public CardApplicationResponse getByOib(@PathVariable String oib) {
    return cardApplicationResourceService.getDetails(oib);
  }

  @Operation(
      summary = "Kreiranje zahtjeva za izdavanjem kartice",
      description = "Vraća objekt dodanog zahtjeva za izdavanjem kartice.")
  @PostMapping
  @LogExecutionTime
  public CardApplicationResponse save(
      @RequestBody @Valid CardApplicationRequest cardApplicationRequest) {
    return cardApplicationResourceService.save(cardApplicationRequest);
  }

  @Operation(
      summary = "Proslijeđivanje zahtjeva po OIB-u",
      description =
          "Proslijeđuje zahtjev za izdavanjem kartice na vanjski API za kreiranje kartice. Vraća status ili grešku u istom formatu u kojem ih vraća vanjski API.")
  @PostMapping("/forward/{oib}")
  @LogExecutionTime
  public ResponseEntity<?> forward(@PathVariable String oib) {
    cardApplicationResourceService.forward(oib);
    return ResponseEntity.status(201).build();
  }

  @Operation(
      summary = "Brisanje po OIB-u",
      description = "Vraća grešku ukoliko ne postoji zahtjev sa zadanim oib-om.")
  @DeleteMapping("/{oib}")
  @LogExecutionTime
  public void delete(@PathVariable String oib) {
    cardApplicationResourceService.delete(oib);
  }
}
