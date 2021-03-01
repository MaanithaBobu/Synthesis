package com.synthesis.hiring.test.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthesis.hiring.test.model.ContactInfoResponse;
import com.synthesis.hiring.test.model.ContactInformation;
import com.synthesis.hiring.test.service.ContactInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactsControllerTest {

  @MockBean
  ContactInfoService contactInfoService;
  @Autowired
  ContactsController contactsController;

  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void contextLoads() {
    assertThat(contactsController).isNotNull();
  }

  @Test
  public void sendValidContactInfoTest() throws Exception {

    String contactInfoJsonString = "{\"firstName\":\"Joe\",\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"street\":\"594 Mortimer dr\",\"city\":\"Cambridge\",\"province_state\":\"ON\",\"country\":\"Canada\",\"postal_code\":\"N3H5R7\"}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    doReturn(ContactInfoResponse.builder().contactInformation(contactInformation).statusCode(200).message("").violations(null).build()).when(contactInfoService).saveContactInfo(any());
    mockMvc.perform(post("/contacts")
        .content(contactInfoJsonString)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(contactInfoService, VerificationModeFactory.times(1))
        .saveContactInfo(any());
    reset(contactInfoService);
  }

  @Test
  public void sendInValidNameContactInfoTest() throws Exception {

    String contactInfoJsonString = "{\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"street\":\"594 Mortimer dr\",\"city\":\"Cambridge\",\"province_state\":\"ON\",\"country\":\"Canada\",\"postal_code\":\"N3H5R7\"}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    mockMvc.perform(post("/contacts")
        .content(contactInfoJsonString)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(contactInfoService, VerificationModeFactory.times(0))
        .saveContactInfo(any());
    reset(contactInfoService);
  }
  @Test
  public void sendInValidAddressStreetContactInfoTest() throws Exception {

    String contactInfoJsonString = "{\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"city\":\"Cambridge\",\"province_state\":\"ON\",\"country\":\"Canada\",\"postal_code\":\"N3H5R7\"}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    mockMvc.perform(post("/contacts")
        .content(contactInfoJsonString)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(contactInfoService, VerificationModeFactory.times(0))
        .saveContactInfo(any());
    reset(contactInfoService);
  }
  @Test
  public void sendInValidAddressContactInfoTest() throws Exception {

    String contactInfoJsonString = "{\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    mockMvc.perform(post("/contacts")
        .content(contactInfoJsonString)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(contactInfoService, VerificationModeFactory.times(0))
        .saveContactInfo(any());
    reset(contactInfoService);
  }
  @Test
  public void sendInvalidCountryContactInfoTest() throws Exception {

    String contactInfoJsonString = "{\"firstName\":\"Joe\",\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"street\":\"594 Mortimer dr\",\"city\":\"Cambridge\",\"province_state\":\"AZ\",\"country\":\"India\",\"postal_code\":\"N3H5R7\"}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    doReturn(ContactInfoResponse.builder().contactInformation(contactInformation).statusCode(200).message("").violations(null).build()).when(contactInfoService).saveContactInfo(any());
    mockMvc.perform(post("/contacts")
        .content(contactInfoJsonString)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(contactInfoService, VerificationModeFactory.times(0))
        .saveContactInfo(any());
    reset(contactInfoService);
  }

  @Test
  public void sendIncorrectAddressContactInfoTest() throws Exception {

    String contactInfoJsonString = "{\"firstName\":\"Joe\",\"lastName\":\"Doe\",\"emailAddress\":\"john.doe@gmail.com\",\"address\":{\"street\":\"123 invalid dr\",\"city\":\"Cambridge\",\"province_state\":\"BC\",\"country\":\"Canada\",\"postal_code\":\"N3H5R7\"}}";
    ContactInformation contactInformation = this.objectMapper.readValue(contactInfoJsonString,ContactInformation.class);
    doReturn(ContactInfoResponse.builder().contactInformation(contactInformation).statusCode(200).message("").violations(null).build()).when(contactInfoService).saveContactInfo(any());
    mockMvc.perform(post("/contacts")
        .content(contactInfoJsonString)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    verify(contactInfoService, VerificationModeFactory.times(0))
        .saveContactInfo(any());
    reset(contactInfoService);
  }
}