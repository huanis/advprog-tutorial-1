package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    private Map<String, String> paymentData = new HashMap<>();

    @Test
    void testCreatePaymentSuccess(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                "VOUCHER_CODE", "SUCCESS", paymentData);

        assertEquals("13652556-012a-4c07-b546-54eb1396d79b", payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentInvalidMethod(){
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                    "INVALID", "SUCCESS", paymentData);
        });
    }

    @Test
    void testCreatePaymentInvalidStatus(){
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                    "VOUCHER_CODE", "INVALID", paymentData);
        });
    }

    @Test
    void testSetStatusPendingToRejected(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                "VOUCHER_CODE", "PENDING", paymentData);

        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusNotPendingToOther(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                "VOUCHER_CODE", "SUCCESS", paymentData);

        assertThrows(IllegalStateException.class, () -> payment.setStatus("REJECTED"));
    }

    @Test
    void testSetStatusToInvalidStatus(){
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b",
                "VOUCHER_CODE", "PENDING", paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("INVALID"));
    }
}
