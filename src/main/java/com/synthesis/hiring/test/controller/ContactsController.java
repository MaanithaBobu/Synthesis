package com.synthesis.hiring.test.controller;

import com.synthesis.hiring.test.model.ContactInfoResponse;
import com.synthesis.hiring.test.model.ContactInformation;
import com.synthesis.hiring.test.service.ContactInfoService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
public class ContactsController {
  private final ContactInfoService contactInfoService;
  public ContactsController(ContactInfoService contactInfoService)
  {
    this.contactInfoService = contactInfoService;
  }
  @PostMapping(value = "/contacts")
  public ResponseEntity<ContactInfoResponse> createContact (@Valid @RequestBody ContactInformation contactInformation) {
    ContactInfoResponse contactInfoResponse = this.contactInfoService.saveContactInfo(contactInformation);
    return ResponseEntity.status(contactInfoResponse.getStatusCode()).body(contactInfoResponse);
  }

}
