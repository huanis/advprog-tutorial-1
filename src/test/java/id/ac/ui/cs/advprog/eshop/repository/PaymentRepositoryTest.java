package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {

    PaymentRepository paymentRepository;

    List<Payment> payments;

    Map<String, String> paymentData = new HashMap<>();

    @BeforeEach
    void setUp(){
        paymentRepository = new PaymentRepository();

        payments = new ArrayList<>();

        Payment payment1 = new Payment("13652556-012a-4c07-b546-54eb1396d79b", PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);
        payments.add(payment1);

        Payment payment2 = new Payment("7f9e15bb-4b15-42f4-aebc-c3af385fb078", PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate(){
        Payment payment = payments.get(1);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testSaveUpdate(){
        Payment payment = payments.get(1);
        paymentRepository.save(payment);

        Payment newPayment = new Payment(payment.getId(), payment.getMethod(),
                PaymentStatus.SUCCESS.getValue(), payment.getPaymentData());
        Payment result = paymentRepository.save(newPayment);

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), findResult.getStatus());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testFindByIdIfIdFound(){
        for (Payment payment : payments){
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findResult.getId());
        assertEquals(payments.get(1).getMethod(), findResult.getMethod());
        assertEquals(payments.get(1).getStatus(), findResult.getStatus());
        assertEquals(payments.get(1).getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testFindByIdIfIdNotFound(){
        for (Payment payment : payments){
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById("zczc");
        assertNull(findResult);
    }

    @Test
    void testFindAllNotEmpty(){
        for (Payment payment : payments){
            paymentRepository.save(payment);
        }

        Iterator<Payment> paymentIterator = paymentRepository.findAll();

        for (Payment payment : payments){
            assertEquals(payment.getId(), paymentIterator.next().getId());
        }
    }

    @Test
    void testFindAllEmpty(){
        Iterator<Payment> paymentIterator = paymentRepository.findAll();
        assertFalse(paymentIterator.hasNext());
    }
}
