package hr.rba.credit_card_client_service.feature.card_application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hr.rba.credit_card_client_service.configuration.feign.card_creation_api.CardCreationApiClient;
import hr.rba.credit_card_client_service.configuration.validation.ValidationException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CardApplicationResourceServiceTest {

    @Mock
    private CardApplicationRepository cardApplicationRepository;

    @Mock
    private CardApplicationMapper cardApplicationMapper;

    @Mock
    private CardCreationApiClient cardCreationApiClient;

    @InjectMocks
    private CardApplicationResourceService cardApplicationResourceService;


    @Test
    void testFindByOibSuccess() {
        String oib = "11111111119";
        CardApplication cardApplication = mock(CardApplication.class);

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));

        Optional<CardApplication> result = cardApplicationResourceService.findByOib(oib);

        assertTrue(result.isPresent());
        assertEquals(cardApplication, result.get());
        verify(cardApplicationRepository, times(1)).findByOib(oib);
    }

    @Test
    void testFindByOibNotFound() {
        String oib = "11111111119";

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.empty());

        Optional<CardApplication> result = cardApplicationResourceService.findByOib(oib);

        assertFalse(result.isPresent());
        verify(cardApplicationRepository, times(1)).findByOib(oib);
    }

    @Test
    void testGetDetailsSuccess() {
        String oib = "11111111119";
        CardApplication cardApplication = mock(CardApplication.class);
        CardApplicationResponse cardApplicationResponse = mock(CardApplicationResponse.class);

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplicationMapper.toResponse(cardApplication)).thenReturn(cardApplicationResponse);

        CardApplicationResponse result = cardApplicationResourceService.getDetails(oib);

        assertNotNull(result);
        assertEquals(cardApplicationResponse, result);
        verify(cardApplicationRepository, times(1)).findByOib(oib);
        verify(cardApplicationMapper, times(1)).toResponse(cardApplication);
    }

    @Test
    void testGetDetailsNotFound() {
        String oib = "11111111119";

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.empty());

        CardApplicationResponse result = cardApplicationResourceService.getDetails(oib);

        assertNull(result);
        verify(cardApplicationRepository, times(1)).findByOib(oib);
        verify(cardApplicationMapper, times(0)).toResponse(any(CardApplication.class));
    }

    @Test
    void testSaveSuccess() {
        CardApplicationRequest cardApplicationRequest = mock(CardApplicationRequest.class);
        CardApplication cardApplication = mock(CardApplication.class);
        CardApplicationResponse cardApplicationResponse = mock(CardApplicationResponse.class);

        when(cardApplicationMapper.toEntity(cardApplicationRequest)).thenReturn(cardApplication);
        when(cardApplication.getOib()).thenReturn("11111111119");
        when(cardApplicationRepository.findByOib("11111111119")).thenReturn(Optional.empty());
        when(cardApplicationRepository.save(cardApplication)).thenReturn(cardApplication);
        when(cardApplicationMapper.toResponse(cardApplication)).thenReturn(cardApplicationResponse);

        CardApplicationResponse result = cardApplicationResourceService.save(cardApplicationRequest);

        assertNotNull(result);
        assertEquals(cardApplicationResponse, result);
        verify(cardApplicationRepository, times(1)).save(cardApplication);
    }

    @Test
    void testSaveOibAlreadyExists() {
        CardApplicationRequest cardApplicationRequest = mock(CardApplicationRequest.class);
        CardApplication cardApplication = mock(CardApplication.class);

        when(cardApplicationMapper.toEntity(cardApplicationRequest)).thenReturn(cardApplication);
        when(cardApplication.getOib()).thenReturn("11111111119");
        when(cardApplicationRepository.findByOib("11111111119")).thenReturn(Optional.of(cardApplication));

        assertThrows(ValidationException.class, () -> cardApplicationResourceService.save(cardApplicationRequest));
    }

    @Test
    void testDeleteSuccess() {
        String oib = "11111111119";
        CardApplication cardApplication = mock(CardApplication.class);

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));

        cardApplicationResourceService.delete(oib);

        verify(cardApplicationRepository, times(1)).deleteById(cardApplication.getId());
    }

    @Test
    void testDeleteNotFound() {
        String oib = "11111111119";

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> cardApplicationResourceService.delete(oib));
    }

    @Test
    void testForwardSuccess() {
        String oib = "11111111119";
        CardApplication cardApplication = mock(CardApplication.class);
        CardApplicationRequest cardApplicationRequest = mock(CardApplicationRequest.class);

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplicationMapper.toRequest(cardApplication)).thenReturn(cardApplicationRequest);

        cardApplicationResourceService.forward(oib);

        verify(cardCreationApiClient, times(1)).createNewCard(cardApplicationRequest);
    }

    @Test
    void testForwardNotFound() {
        String oib = "11111111119";

        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> cardApplicationResourceService.forward(oib));
    }

    @Test
    void testChangeStatusSuccess() {
        String oib = "11111111119";
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);
        CardApplication cardApplication = mock(CardApplication.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(oib);
        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplication.getStatus()).thenReturn(CardApplication.Status.Pending);
        when(map.get("status")).thenReturn("Approved");

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplication).setStatus(CardApplication.Status.Approved);
        verify(cardApplicationRepository, times(1)).save(cardApplication);
    }

    @Test
    void testChangeStatusKafkaListenerInvalidStatusFormat() {
        String oib = "11111111119";
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);
        CardApplication cardApplication = mock(CardApplication.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(oib);
        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplication.getStatus()).thenReturn(CardApplication.Status.Pending);
        when(map.get("status")).thenReturn("INVALID_STATUS");

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplication, times(0)).setStatus(any(CardApplication.Status.class));
        verify(cardApplicationRepository, times(0)).save(any(CardApplication.class));
    }


    @Test
    void testChangeStatusOibNull() {
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(null);

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplicationRepository, times(0)).findByOib(anyString());
        verify(cardApplicationRepository, times(0)).save(any(CardApplication.class));
    }

    @Test
    void testChangeStatusOibNotFound() {
        String oib = "11111111119";
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(oib);
        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.empty());

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplicationRepository, times(1)).findByOib(oib);
        verify(cardApplicationRepository, times(0)).save(any(CardApplication.class));
    }

    @Test
    void testChangeStatusStatusNotPending() {
        String oib = "11111111119";
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);
        CardApplication cardApplication = mock(CardApplication.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(oib);
        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplication.getStatus()).thenReturn(CardApplication.Status.Approved);

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplication, times(0)).setStatus(any(CardApplication.Status.class));
        verify(cardApplicationRepository, times(0)).save(cardApplication);
    }

    @Test
    void testChangeStatusNewStatusPending() {
        String oib = "11111111119";
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);
        CardApplication cardApplication = mock(CardApplication.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(oib);
        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplication.getStatus()).thenReturn(CardApplication.Status.Pending);
        when(map.get("status")).thenReturn("Pending");

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplication, times(0)).setStatus(CardApplication.Status.Pending);
        verify(cardApplicationRepository, times(0)).save(cardApplication);
    }

    @Test
    void testChangeStatusKafkaListenerMissingStatus() {
        String oib = "11111111119";
        ConsumerRecord<Void, Map<String, String>> record = mock(ConsumerRecord.class);
        Map<String, String> map = mock(Map.class);
        CardApplication cardApplication = mock(CardApplication.class);

        when(record.value()).thenReturn(map);
        when(map.get("oib")).thenReturn(oib);
        when(cardApplicationRepository.findByOib(oib)).thenReturn(Optional.of(cardApplication));
        when(cardApplication.getStatus()).thenReturn(CardApplication.Status.Pending);
        when(map.get("status")).thenReturn(null);

        cardApplicationResourceService.changeStatus(record);

        verify(cardApplication, times(0)).setStatus(any(CardApplication.Status.class));
        verify(cardApplicationRepository, times(0)).save(any(CardApplication.class));
    }}