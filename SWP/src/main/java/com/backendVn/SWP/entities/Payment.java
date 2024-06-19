package com.backendVn.SWP.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @Column(name = "PaymentID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RequestID")
    private Request requestID;

    @Column(name = "Amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("getdate()")
    @Column(name = "PaymentDate")
    private Instant paymentDate;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PaymentType", length = 50)
    private String paymentType;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Status", length = 50)
    private String status;

}