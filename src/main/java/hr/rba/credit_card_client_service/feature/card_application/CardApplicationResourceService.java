package hr.rba.credit_card_client_service.feature.card_application;


import hr.rba.credit_card_client_service.configuration.feign.card_creation_api.CardCreationApiClient;
import hr.rba.credit_card_client_service.configuration.validation.ValidationException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CardApplicationResourceService {

  private final static String KAFKA_TOPIC_STATUS_CHANGE = "credit-card-client-service.status-change";
  private final CardApplicationRepository cardApplicationRepository;
  private final CardApplicationMapper cardApplicationMapper;
  private final CardCreationApiClient cardCreationApiClient;

  public Optional<CardApplication> findByOib(String oib) {
    return this.cardApplicationRepository.findByOib(oib);
  }

  public CardApplicationResponse getDetails(String oib) {
    Optional<CardApplication> cardApplication = findByOib(oib);
    return cardApplication.isPresent()? cardApplicationMapper.toResponse(cardApplication.get()):null;
  }

  public CardApplicationResponse save(CardApplicationRequest cardApplicationRequest) {
    CardApplication cardApplication =
        cardApplicationMapper.toEntity(cardApplicationRequest);
    if (findByOib(cardApplication.getOib()).isPresent())
      throw new ValidationException("cardApplication.oibAlreadyExists");
    cardApplicationRepository.save(cardApplication);
    return cardApplicationMapper.toResponse(cardApplication);
  }

  @KafkaListener(
          topics = KAFKA_TOPIC_STATUS_CHANGE,
          containerFactory = "kafkaListenerContainerFactoryStatusChange",
          concurrency = "1")
  public void changeStatus(ConsumerRecord<Void, Map<String, String>> record) {
    log.info("Starting execution method changeStatus");
    String oib = record.value().get("oib");
    if(oib == null){
      log.info("Oib is null, aborting status change.");
      return;
    }
    Optional<CardApplication> cardApplication = findByOib(oib);
    if(cardApplication.isEmpty()){
      log.info("Oib not found, aborting status change.");
      return;
    }
    if(cardApplication.get().getStatus() != CardApplication.Status.Pending){
      log.info("Card application currently not pending, aborting status change.");
      return;
    }
    CardApplication.Status status;
    try{
      status = Enum.valueOf(CardApplication.Status.class,record.value().get("status"));
    }
    catch (IllegalArgumentException e){
      log.info("New status not in correct format, aborting status change.");
      return;
    }
    catch (NullPointerException e){
      log.info("New status missing, aborting status change.");
      return;
    }
    if (status == CardApplication.Status.Pending){
      log.info("New status can not be Pending, aborting status change.");
      return;
    }
    cardApplication.get().setStatus(status);
    cardApplicationRepository.save(cardApplication.get());
    log.info("Status successfully changed. Exiting method changeStatus");
  }


  public void delete(String oib) {
    CardApplication cardApplication = findByOib(oib).orElseThrow(()-> new ValidationException("cardApplication.notFound"));
    cardApplicationRepository.deleteById(cardApplication.getId());
  }

  public void forward(String oib) {
    CardApplication cardApplication = findByOib(oib).orElseThrow(()-> new ValidationException("cardApplication.notFound"));
    cardCreationApiClient.createNewCard(cardApplicationMapper.toRequest(cardApplication));
  }


}
