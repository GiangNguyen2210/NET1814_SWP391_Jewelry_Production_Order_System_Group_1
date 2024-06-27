package com.backendVn.SWP.repositories;

import com.backendVn.SWP.entities.Request;
import com.backendVn.SWP.entities.RequestOrder;
import com.backendVn.SWP.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface RequestOrderRepository extends JpaRepository<RequestOrder, Integer> {
    List<RequestOrder> findAllByDesignStaff(User designStaff);
    List<RequestOrder> findByRequestID(Request request);
    List<RequestOrder> findByCreatedAtBetween(Instant start, Instant end);
}