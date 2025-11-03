package me.laurelmay.game24.rest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = OperandResponse.Number.class, name = "number"),
  @JsonSubTypes.Type(value = OperandResponse.Expression.class, name = "expression")
})
public sealed interface OperandResponse {
  record Number(int value) implements OperandResponse {
  }

  record Expression(OperationResponse operation) implements OperandResponse {
  }
}
