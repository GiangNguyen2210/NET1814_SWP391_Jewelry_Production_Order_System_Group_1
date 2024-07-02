package com.backendVn.SWP.controllers;


import com.backendVn.SWP.dtos.request.DesignCreationRequest;
import com.backendVn.SWP.dtos.request.DesignUpdateRequest;
import com.backendVn.SWP.dtos.response.ApiResponse;
import com.backendVn.SWP.dtos.response.DesignResponse;
import com.backendVn.SWP.services.DesignService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/design")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DesignController {
    DesignService designService;

    @PostMapping("/{requestOrderId}")
    public ApiResponse<DesignResponse> createDesign(@RequestBody @Valid DesignCreationRequest designCreationRequest, @PathVariable Integer requestOrderId) {
        DesignResponse designResponse = designService.createDesign(designCreationRequest, requestOrderId);
        return ApiResponse.<DesignResponse>builder()
                .result(designResponse)
                .build();
    }

    @PutMapping("/{designId}")
    public ApiResponse<DesignResponse> updateDesign(@PathVariable Integer designId, @RequestBody @Valid DesignUpdateRequest designUpdateRequest) {
        DesignResponse designResponse = designService.updateDesign(designId, designUpdateRequest);
        return ApiResponse.<DesignResponse>builder()
                .result(designResponse)
                .build();
    }


    @GetMapping
    public ApiResponse<List<DesignResponse>> getAllDesigns() {
        List<DesignResponse> designResponse = designService.getAllDesign();
        return ApiResponse.<List<DesignResponse>>builder()
                .result(designResponse)
                .build();
    }

    @GetMapping("/{requestOrderId}")
    public ApiResponse<DesignResponse> getDesignById(@PathVariable Integer requestOrderId) {
        DesignResponse designResponse = designService.getDesignById(requestOrderId);
        return ApiResponse.<DesignResponse>builder()
                .result(designResponse)
                .build();
    }

}