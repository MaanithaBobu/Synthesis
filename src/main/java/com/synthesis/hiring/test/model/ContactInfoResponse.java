package com.synthesis.hiring.test.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInfoResponse {

  private final ContactInformation contactInformation;
  private final String message;
  private final int statusCode;
  private List<Violation> violations = new ArrayList<>();

  public ContactInfoResponse(String message, int statusCode) {
    this.message = message;
    this.statusCode = statusCode;
    this.contactInformation = null;
  }

  public ContactInfoResponse(ContactInformation contactInformation, String message, int statusCode,
      List<Violation> violations) {
    this.contactInformation = contactInformation;
    this.message = message;
    this.statusCode = statusCode;
    this.violations = violations;
  }
}
