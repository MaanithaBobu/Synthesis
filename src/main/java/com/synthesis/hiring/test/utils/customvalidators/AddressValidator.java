package com.synthesis.hiring.test.utils.customvalidators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synthesis.hiring.test.model.Address;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.client.RestTemplate;


public class AddressValidator implements ConstraintValidator<ValidSynthesisAddress, Address> {
  private static final String API_KEY = "b07b4881-512f-410b-8c27-2bb82f44c2dc";
  private static final String API_URL = "https://www.fixaddress.com/SACAPI/bulkFromCode";
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();
  public void initialize(ValidSynthesisAddress validSynthesisAddress) {
  }

  public boolean isValid(Address address, ConstraintValidatorContext context) {
    HashSet<String> incorrectFields = new HashSet<>();
    if(address.getPostalCode() ==null || address.getPostalCode().length()<5)
      incorrectFields.add("postal_code");
    if(address.getCountry() ==null || address.getCountry().length()<3)
      incorrectFields.add("country");
    if(address.getProvinceState() ==null || address.getProvinceState().length()!=2)
      incorrectFields.add("province_state");
    if(address.getCity() ==null || address.getCity().length()<2)
      incorrectFields.add("city");
    if(address.getStreet() ==null || address.getStreet().length()<3)
      incorrectFields.add("postal_code");

    if (incorrectFields.size()>0)
    {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(String.format("missing/incorect fields for %s",incorrectFields.parallelStream().collect(
          Collectors.joining(", ")))).addConstraintViolation();
      return false;
    }
    if(!(address.getCountry().equalsIgnoreCase("Canada") || address.getCountry().equalsIgnoreCase("USA")))
    {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(String.format("Invalid Country. Only USA and Canada are supported, %s is not supported yet.",address.getCountry())).addConstraintViolation();
      return false;
    }
    ArrayNode addressJson = objectMapper.createArrayNode();
    addressJson.add("");
    addressJson.add(address.getStreet());
    addressJson.add("");
    addressJson.add(address.getCity());
    addressJson.add(address.getProvinceState());
  //  addressJson.add(address.getCountry());
    addressJson.add(address.getPostalCode());
    ArrayNode addressesHolder = objectMapper.createArrayNode();
    addressesHolder.add(addressJson);
    ObjectNode payload = objectMapper.createObjectNode();
    payload.put("region_code",address.getCountry().equalsIgnoreCase("Canada")?"CAN":"USA" );
    payload.put("api_key",API_KEY);
    payload.set("address",addressesHolder);
    try {
      // due to the service we are using we can't directly parse into  object node as the type being set by service is text/html
     //JsonNode response= restTemplate.postForObject(new URI(API_URL),objectMapper.writeValueAsString(payload),JsonNode.class);
      JsonNode response= objectMapper.readTree(restTemplate.postForObject(new URI(API_URL),objectMapper.writeValueAsString(payload),String.class));
     //15th field is hitscore so we are deciding the address is correct if the hitscore is greater than 90 and not if it's less than that.
      if(response.get("results").get(0).get(0).get(0).get(14).asDouble()>90)
        return true;
      else
      {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("In correct address. please verify the address provided"+response.toString()).addConstraintViolation();
        return false;
      }

    } catch (Exception e) {
      e.printStackTrace();
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("got exception while using address validation service. please try after some time.").addConstraintViolation();
      return false;
    }
  }


}
