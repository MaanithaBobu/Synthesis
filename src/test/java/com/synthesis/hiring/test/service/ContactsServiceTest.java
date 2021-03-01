package com.synthesis.hiring.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthesis.hiring.test.dao.ContactInfoRepository;
import com.synthesis.hiring.test.model.ContactInfoResponse;
import com.synthesis.hiring.test.model.ContactInformation;
import java.util.Optional;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContactsServiceTest {
  @MockBean
  private ContactInfoRepository contactInfoRepository;
  @Autowired
  ContactInfoService contactInfoService;
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testSave() throws JsonProcessingException {
    String contactInfoJsonString = "{\"firstName\":\"Joe\",\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"street\":\"594 Mortimer dr\",\"city\":\"Cambridge\",\"province_state\":\"ON\",\"country\":\"Canada\",\"postal_code\":\"N3H5R7\"}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    when(contactInfoRepository.findById(any())).thenReturn(Optional.empty());
    when(contactInfoRepository.save(any())).thenReturn(contactInformation);
    ContactInfoResponse contactInfoResponse = contactInfoService.saveContactInfo(contactInformation);
    UUID uuid = UUID.fromString("8360a774-c363-3eb5-b9cb-8ea3f0e844dc");
    Assertions.assertEquals( uuid , contactInfoResponse.getContactInformation().getAccountId());
    Assertions.assertEquals(200,contactInfoResponse.getStatusCode());
    Assertions.assertNull(contactInfoResponse.getViolations());
    Mockito.verify(contactInfoRepository,times(1)).findById(any());
    Mockito.verify(contactInfoRepository,times(1)).save(any());
    reset(contactInfoRepository);
  }


  @Test
  void testDuplicateSave() throws JsonProcessingException {
    String contactInfoJsonString = "{\"firstName\":\"Joe\",\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"street\":\"594 Mortimer dr\",\"city\":\"Cambridge\",\"province_state\":\"ON\",\"country\":\"Canada\",\"postal_code\":\"N3H5R7\"}}";

    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    when(contactInfoRepository.findById(any())).thenReturn(Optional.of(contactInformation));
    when(contactInfoRepository.save(any())).thenReturn(contactInformation);
    ContactInfoResponse contactInfoResponse = contactInfoService.saveContactInfo(contactInformation);
    UUID uuid = UUID.fromString("8360a774-c363-3eb5-b9cb-8ea3f0e844dc");
    Assertions.assertEquals( uuid , contactInfoResponse.getContactInformation().getAccountId());
    Assertions.assertEquals(409,contactInfoResponse.getStatusCode());
    Assertions.assertNotNull(contactInfoResponse.getViolations());
    Assertions.assertEquals(1,contactInfoResponse.getViolations().size());
    Assertions.assertEquals(uuid,UUID.fromString(contactInfoResponse.getViolations().get(0).getRejectedValue()));
    Mockito.verify(contactInfoRepository,times(1)).findById(any());
    Mockito.verify(contactInfoRepository,times(0)).save(any());
    reset(contactInfoRepository);
  }

}
