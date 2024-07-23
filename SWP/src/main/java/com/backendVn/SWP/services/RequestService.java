package com.backendVn.SWP.services;

import com.backendVn.SWP.dtos.request.RequestCreationRequestForCustomerDesign;
import com.backendVn.SWP.dtos.response.RequestResponse;
import com.backendVn.SWP.dtos.response.UserResponse;
import com.backendVn.SWP.entities.Design;
import com.backendVn.SWP.entities.Material;
import com.backendVn.SWP.entities.Request;
import com.backendVn.SWP.entities.User;
import com.backendVn.SWP.exception.AppException;
import com.backendVn.SWP.exception.ErrorCode;
import com.backendVn.SWP.mappers.RequestMapper;
import com.backendVn.SWP.mappers.UserMapper;
import com.backendVn.SWP.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    private static final Logger log = LoggerFactory.getLogger(RequestService.class);
    RequestRepository requestRepository;
    UserRepository userRepository;
    RequestMapper requestMapper;
    UserMapper userMapper;
    MaterialRepository materialRepository;
    DesignRepository designRepository;
    private final DesignService designService;

    public Instant stringToInstant(String input){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(input, formatter);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public BigDecimal makeProduceCost(String input) {
        return switch (input) {
            case "RING" -> BigDecimal.valueOf(1500);
            case "NECKLACE" -> BigDecimal.valueOf(2200);
            case "BRACELET" -> BigDecimal.valueOf(2000);
            default -> BigDecimal.valueOf(1800);
        };
    }


    public RequestResponse createRequest(RequestCreationRequestForCustomerDesign request, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(user.getAddress() == null ||
                user.getCusName() == null ||
                user.getPhoneNum() == null ||
                user.getEmail() == null){
            throw new AppException(ErrorCode.CAN_NOT_REQUEST);
        }

        Request theRequest = requestMapper.toRequest(request);
        theRequest.setCustomerID(user);

        Material goldMaterial = findOrCreateGoldMaterial(request);

        theRequest.setMaterialID(goldMaterial);
        theRequest.setMainStone(getMaterialById(request.getMainStoneId()));
        theRequest.setSubStone(getMaterialById(request.getSubStoneId()));
        theRequest.setProduceCost(makeProduceCost(request.getCategory()));
        theRequest.setURLImage(designService.createCSV(request.getListURLImage()));
        theRequest.setStatus("Unapproved");

        return requestMapper.toRequestResponse(requestRepository.save(theRequest));
    }

    public RequestResponse createRequestWithCompanyDesign(Integer userId, Integer companyDesignId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(user.getAddress() == null ||
                user.getCusName() == null ||
                user.getPhoneNum() == null ||
                user.getEmail() == null){
            throw new AppException(ErrorCode.CAN_NOT_REQUEST);
        }

        Design design = designRepository.findById(companyDesignId)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_NOT_FOUND));

        List<Material> materials = materialRepository.findByMaterialName(design.getMaterialName())
                .orElseThrow(() -> new AppException(ErrorCode.MATERIAL_NOT_FOUND));

        materials.sort(Comparator.comparing(Material::getUpdateTime));

        Request request = Request.builder()
                .companyDesign(design)
                .createdAt(Instant.now())
                .customerID(user)
                .category(design.getCategory())
                .produceCost(makeProduceCost(design.getCategory()))
                .mainStone(design.getMainStone())
                .subStone(design.getSubStone())
                .materialWeight(design.getMaterialWeight())
                .materialID(materials.getLast())
                .status("Unapproved")
                .build();

        requestRepository.save(request);

        return requestMapper.toRequestResponse(request);
    }

    private Material getMaterialById(Integer materialId) {
        return materialId == 0 ? null : materialRepository.findById(materialId)
                .orElseThrow(() -> new AppException(ErrorCode.MATERIAL_NOT_FOUND));
    }

    private Material findOrCreateGoldMaterial(RequestCreationRequestForCustomerDesign request) {
        return materialRepository.findByMaterialNameAndUpdateTime(
                        request.getGoldType(), stringToInstant(request.getUpdated()))
                .orElseGet(() -> {
                    Material newGoldType = new Material();
                    newGoldType.setMaterialName(request.getGoldType());
                    newGoldType.setType("Gold");
                    newGoldType.setUpdateTime(stringToInstant(request.getUpdated()));
                    newGoldType.setPricePerUnit(BigDecimal.valueOf(request.getSellCost()));
                    return materialRepository.save(newGoldType);
                });
    }

    public RequestResponse updateRequestByCustomer(Integer id, RequestCreationRequestForCustomerDesign requestCreationRequestForCustomerDesign) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if ("Pending Quotation".equalsIgnoreCase(request.getStatus())) {
            throw new AppException(ErrorCode.REQUEST_STATUS_NOT_ALLOWED);
        }

        requestMapper.updateRequestFromDto(request, requestCreationRequestForCustomerDesign);

        return requestMapper.toRequestResponse(requestRepository.save(request));
    }

    public RequestResponse updateRequestBySales(Integer id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User saleStaff = userRepository.findByUserName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        request.setSaleStaffid(saleStaff);
        request.setRecievedAt(Instant.now());
        request.setStatus("Processing");

        return requestMapper.toRequestResponse(requestRepository.save(request));
    }


    public void deleteRequest(Integer id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        request.setStatus("Disable");
        requestRepository.save(request);
    }

    public List<RequestResponse> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(requestMapper::toRequestResponse)
                .toList();
    }

    public RequestResponse getRequestById(Integer id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        return requestMapper.toRequestResponse(request);
    }

    public List<RequestResponse> getRequestsByCustomerId(Integer customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Request> requests = requestRepository.findAllByCustomerIDAndStatusIsNotLike(customer, "Disable");
        return requests.stream()
                .map(requestMapper::toRequestResponse)
                .toList();
    }

    public List<RequestResponse> getUnrecievedRequests() {
        return requestRepository.findAllBySaleStaffidNull().stream()
                .map(requestMapper::toRequestResponse)
                .toList();
    }

    public List<RequestResponse> getRequestBySaleStaffId(Integer saleStaffId){
        User user = userRepository.findById(saleStaffId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return requestRepository.findAllBySaleStaffid(user).stream()
                .map(requestMapper::toRequestResponse)
                .toList();
    }

    public UserResponse getUserById(Integer id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public RequestResponse  approveQuotationFromCustomer(Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        request.setStatus("Ordering");

        Request savedRequest = requestRepository.save(request);

        return requestMapper.toRequestResponse(savedRequest);
    }

    public RequestResponse denyQuotationFromCustomer(Integer requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        request.setStatus("Denied");

        Request savedRequest = requestRepository.save(request);

        return requestMapper.toRequestResponse(savedRequest);
    }

    public List<RequestResponse> getListOfRequestQuotations() {
        List<Request> requests = requestRepository.findByStatus("Pending quotation for manager");
        return requests.stream().map(requestMapper::toRequestResponse).toList();
    }
}
