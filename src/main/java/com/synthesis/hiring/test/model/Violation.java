package com.synthesis.hiring.test.model;
import lombok.Data;

@Data
public class Violation  {
private final String fieldName;
private final String message;
private final String rejectedValue;
}