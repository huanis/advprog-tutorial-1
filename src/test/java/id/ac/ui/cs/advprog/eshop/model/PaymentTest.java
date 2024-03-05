package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    private Map<String, String> paymentData = new HashMap<>();

    @Test
    void testCreatePaymentSuccess(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                PaymentMethod.VOUCHER_CODE.getValue(), PaymentStatus.SUCCESS.getValue(), paymentData);

        assertEquals("13652556-012a-4c07-b546-54eb1396d79b", payment.getId());
        assertEquals(PaymentMethod.VOUCHER_CODE.getValue(), payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentInvalidMethod(){
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                    "INVALID", PaymentStatus.SUCCESS.getValue(), paymentData);
        });
    }

    @Test
    void testCreatePaymentInvalidStatus(){
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                    PaymentMethod.VOUCHER_CODE.getValue(), "INVALID", paymentData);
        });
    }

    @Test
    void testSetStatusPendingToRejected(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                PaymentMethod.VOUCHER_CODE.getValue(), PaymentStatus.PENDING.getValue(), paymentData);

        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusNotPendingToOther(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                PaymentMethod.VOUCHER_CODE.getValue(), PaymentStatus.SUCCESS.getValue(), paymentData);

        assertThrows(IllegalStateException.class, () -> payment.setStatus(PaymentStatus.REJECTED.getValue()));
    }

    @Test
    void testSetStatusToInvalidStatus(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                PaymentMethod.VOUCHER_CODE.getValue(), PaymentStatus.PENDING.getValue(), paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("INVALID"));
    }
}
