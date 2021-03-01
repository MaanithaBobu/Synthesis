package com.synthesis.hiring.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthesis.hiring.test.dao.ContactInfoRepository;
import com.synthesis.hiring.test.model.ContactInformation;
import com.synthesis.hiring.test.model.ContactInfoResponse;
import com.synthesis.hiring.test.model.Violation;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ContactInfoService {
  private final  ContactInfoRepository contactInfoRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();
  public ContactInfoService(ContactInfoRepository contactInfoRepository){
    this.contactInfoRepository=contactInfoRepository;
  }
  public ContactInfoResponse saveContactInfo(ContactInformation contactInformation)
  {
    contactInformation.setAccountId(this.generateUniqueID(contactInformation));
    Optional<ContactInformation> existingContactInformation = this.contactInfoRepository.findById(contactInformation.getAccountId());
    if(existingContactInformation.isPresent())
      return ContactInfoResponse.builder().statusCode(409).contactInformation(contactInformation).message("provided contact information already exists").violations(
          Collections.singletonList(new Violation("account Identifier","provided account id already exists in database",contactInformation.getAccountId().toString()))).build();

    ContactInformation savedContactInfo = this.contactInfoRepository.save(contactInformation);
    return ContactInfoResponse.builder().contactInformation(savedContactInfo)
        .message("contact info saved successfully").violations(null).statusCode(200).build();
  }

  private UUID generateUniqueID(ContactInformation contactInformation) {
    return UUID.nameUUIDFromBytes(contactInformation.toString().getBytes());
  }
}
