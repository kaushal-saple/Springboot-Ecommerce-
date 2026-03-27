package com.ecommerce.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;

    @NotNull
    @Size(min=3, message = "payment method must contain least 3 character")
    private String paymentMethod;

    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    public Payment(String paymentMethod, String pgStatus, String pgName, String pgResponseMessage, String pgPaymentId) {
        this.paymentMethod = paymentMethod;
        this.pgStatus = pgStatus;
        this.pgName = pgName;
        this.pgResponseMessage = pgResponseMessage;
        this.pgPaymentId = pgPaymentId;
    }
}

