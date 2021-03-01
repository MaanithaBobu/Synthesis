package com.synthesis.hiring.test.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.synthesis.hiring.test.utils.customvalidators.ValidSynthesisAddress;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.ToString;

@Data
@Entity
@ToString
public class ContactInformation {
  @Id
  private UUID accountId;
  @NotBlank(message = "First Name can't be blank")
  @Size( min = 3,message = "first Name need to be at least 3 characters")
  private String firstName;
  @NotBlank(message = "Last Name can't be blank")
  @Size( min = 3,message = "Last Name need to be at least 3 characters")
  private String lastName;
  @Email(message = "need to be valid email address format")
  private String emailAddress;
  @Valid
  @ValidSynthesisAddress
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id")
  private Address address;
}
