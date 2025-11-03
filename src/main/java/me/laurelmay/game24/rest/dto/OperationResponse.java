package me.laurelmay.game24.rest.dto;

public record OperationResponse(OperandResponse lhs, OperandResponse rhs, String operator) {
}
