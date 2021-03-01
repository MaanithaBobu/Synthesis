  package com.synthesis.hiring.test.model;

  import com.fasterxml.jackson.annotation.JsonProperty;
  import javax.persistence.Entity;
  import javax.persistence.GeneratedValue;
  import javax.persistence.GenerationType;
  import javax.persistence.Id;
  import javax.persistence.JoinColumn;
  import lombok.Data;
  import lombok.ToString;

  @Data
  @Entity
  @ToString
  public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private String city;
    @JsonProperty("province_state")
    private String provinceState;
    private String country;
    @JsonProperty("postal_code")
    private String postalCode;
  }
