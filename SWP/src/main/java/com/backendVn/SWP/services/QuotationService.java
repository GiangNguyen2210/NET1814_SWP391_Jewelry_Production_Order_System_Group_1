package com.backendVn.SWP.services;

import com.backendVn.SWP.dtos.request.QuotationCreationRequest;
import com.backendVn.SWP.dtos.request.UserUpdateRequest;
import com.backendVn.SWP.dtos.response.QuotationResponse;
import com.backendVn.SWP.dtos.response.UserResponse;
import com.backendVn.SWP.entities.Quotation;
import com.backendVn.SWP.entities.Request;
import com.backendVn.SWP.entities.User;
import com.backendVn.SWP.exception.AppException;
import com.backendVn.SWP.exception.ErrorCode;
import com.backendVn.SWP.mappers.QuotationMapper;
import com.backendVn.SWP.repositories.QuotationRepository;
import com.backendVn.SWP.repositories.RequestRepository;
import com.backendVn.SWP.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class QuotationService {
    QuotationRepository quotationRepository;
    QuotationMapper quotationMapper;
    RequestRepository requestRepository;
    UserRepository userRepository;

    public QuotationResponse createQuotation(QuotationCreationRequest quotationCreationRequest, Integer requestId){
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        Quotation quotation = quotationMapper.toQuotation(quotationCreationRequest);
        quotation.setRequestID(request);
        quotation.setCreatedAt(Instant.now());

        Quotation savedQuotation = quotationRepository.save(quotation);

        return quotationMapper.toQuotationResponse(savedQuotation);
    }

    public List<QuotationResponse> getAllQuotation() {
        return quotationRepository.findAll().stream()
                .map(quotationMapper::toQuotationResponse).toList();
    }

    public void deleteQuotation(Integer id) {
        quotationRepository.deleteById(id);
    }

    public QuotationResponse updateQuotation(Integer id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUOTATION_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUserName(username).get();

        quotation.setApproveBy(user);
        quotation.setCreatedAt(Instant.now());

        Quotation savedQuotation = quotationRepository.save(quotation);

        return quotationMapper.toQuotationResponse(savedQuotation);
    }
}