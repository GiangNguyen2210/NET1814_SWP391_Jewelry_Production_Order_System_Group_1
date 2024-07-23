package com.backendVn.SWP.mappers;

import com.backendVn.SWP.dtos.response.PaymentResponse;
import com.backendVn.SWP.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "requestID", source = "requestID.id")
    PaymentResponse toPaymentResponse(Payment payment);
}
