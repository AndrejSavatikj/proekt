package com.example.book_store.service;

import com.example.book_store.model.dto.ChargeRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public interface PaymentService {
    Charge pay(ChargeRequest chargeRequest) throws StripeException;

}
