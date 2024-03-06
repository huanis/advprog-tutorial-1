package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    List<Product> products;

    List<Order> orders;

    @BeforeEach
    void setUp(){

        this.products = new ArrayList<>();
        this.products.add(new Product());

        // pending order
        Order order1 = new Order("eb558efc-1c39-460e-8860-71af6af63bd6",
                products, 1708560000L, "Safira Sudrajat");
        // success order
        Order order2 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708550000L, "Safira Sudrajat", OrderStatus.SUCCESS.getValue());
        // cancelled order
        Order order3 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products, 1708550000L, "Safira Sudrajat", OrderStatus.CANCELLED.getValue());
        // failed order
        Order order4 = new Order("ab3177ff-845j-42k3-rw66-oi89rh22u238",
                products, 1708550000L, "Safira Sudrajat", OrderStatus.FAILED.getValue());

        orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
    }

    @Test
    void testAddPaymentVoucherCodeValid(){
        String voucherCode = "ESHOP1234ABC5678";

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", voucherCode);

        Payment payment = new Payment(orders.getFirst().getId(), PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.SUCCESS.getValue(), paymentData);

        doReturn(payment).when(paymentRepository).save(any(Payment.class));
        doReturn(null).when(paymentRepository).findById(anyString());

        doReturn(orders.getFirst()).when(orderService).updateStatus(anyString(), anyString());

        // returns payment
        Payment result = paymentService.addPayment(orders.getFirst(), PaymentMethod.VOUCHER_CODE.getValue(), paymentData);

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getStatus(), result.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeInvalid(){

        List<String> invalidCodes = new ArrayList<>();
        invalidCodes.add(null);
        invalidCodes.add("ESHOP12345678");
        invalidCodes.add("12345678ABCDEFHI");
        invalidCodes.add("ABCDEFGHIJKLMNOP");
        invalidCodes.add("ESHOPABCDEFGH123");
        invalidCodes.add("ESHOPNISA1");
        invalidCodes.add("12345678");
        invalidCodes.add("TEST");

        doReturn(null).when(paymentRepository).findById(anyString());

        // returns null
        Map<String, String> paymentData = new HashMap<>();

        Payment payment = new Payment(orders.getFirst().getId(), PaymentMethod.VOUCHER_CODE.getValue(),
                PaymentStatus.REJECTED.getValue(), paymentData);

        doReturn(payment).when(paymentRepository).save(any(Payment.class));
        doReturn(orders.getFirst()).when(orderService).updateStatus(anyString(), anyString());

        for (String invalidCode : invalidCodes){
            paymentData.put("voucherCode", invalidCode);
            Payment result = paymentService.addPayment(orders.getFirst(), PaymentMethod.VOUCHER_CODE.getValue(), paymentData);

            assertEquals(payment.getId(), result.getId());
            assertEquals(payment.getStatus(), result.getStatus());
        }

    }

    @Test
    void testAddPaymentBankTransferPending(){

        doReturn(null).when(paymentRepository).findById(anyString());

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "DUMMY");
        paymentData.put("referenceCode", "DUMMY");

        Payment payment = new Payment(orders.getFirst().getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData);

        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        // returns payment
        Payment result = paymentService.addPayment(orders.getFirst(), PaymentMethod.BANK_TRANSFER.getValue(), paymentData);

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getStatus(), result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferRejected(){

        doReturn(null).when(paymentRepository).findById(anyString());

        Map<String, String> invalidData1 = new HashMap<>();
        Map<String, String> invalidData2 = new HashMap<>();
        invalidData2.put("bankName", "DUMMY");
        Map<String, String> invalidData3 = new HashMap<>();
        invalidData3.put("referenceCode", "DUMMY");

        List<Map<String, String>> invalidData = new ArrayList<>();
        invalidData.add(invalidData1);
        invalidData.add(invalidData2);
        invalidData.add(invalidData3);

        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708550000L, "Safira Sudrajat");


        // returns payment

        for (Map<String, String> paymentData : invalidData){

            Payment payment = new Payment(order.getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                    PaymentStatus.REJECTED.getValue(), paymentData);

            doReturn(payment).when(paymentRepository).save(any(Payment.class));
            Payment result = paymentService.addPayment(order, PaymentMethod.BANK_TRANSFER.getValue(), paymentData);

            assertEquals(payment.getId(), result.getId());
            assertEquals(payment.getStatus(), result.getStatus());
        }
    }

    @Test
    void testAddPaymentInvalidMethod(){

        // returns null
        Payment result = paymentService.addPayment(orders.getFirst(), "TEST_INVALID", new HashMap<>());

        assertNull(result);
    }

    @Test
    void testAddPaymentOrderHasPayment(){

        // returns null
        Order order = orders.get(3);
        Payment payment = new Payment(order.getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.REJECTED.getValue(), new HashMap<>());

        doReturn(payment).when(paymentRepository).findById(anyString());

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER_CODE.getValue(), new HashMap<>());

        assertNull(result);
    }

    @Test
    void testSetStatusOrderWaitingForPayment(){

        // returns payment

        Order order = orders.getFirst();
        Payment payment = new Payment(order.getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), new HashMap<>());

        doReturn(order).when(orderService).findById(anyString());
        doReturn(order).when(orderService).updateStatus(anyString(), anyString());
        doReturn(payment).when(paymentRepository).findById(anyString());
        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(order.getId(), result.getId());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusOrderNotWaitingForPayment(){

        // returns payment but not changed
        Order order = orders.get(1);
        Payment payment = new Payment(order.getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), new HashMap<>());

        doReturn(order).when(orderService).findById(anyString());
        doReturn(payment).when(paymentRepository).findById(anyString());
        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(order.getId(), result.getId());
        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusPaymentNotExist(){
        Order order = orders.getFirst();
        Payment payment = new Payment(order.getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), new HashMap<>());

        doReturn(order).when(orderService).findById(anyString());
        doReturn(null).when(paymentRepository).findById(anyString());
        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(order.getId(), result.getId());
        assertNotEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testGetPaymentByIdFound(){

        // returns payment
        Payment payment = new Payment(orders.getFirst().getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), new HashMap<>());

        doReturn(payment).when(paymentRepository).findById(anyString());

        Payment result = paymentService.getPayment("TESTING");

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentByIdNotFound(){

        // returns null
        doReturn(null).when(paymentRepository).findById(anyString());

        Payment result = paymentService.getPayment("TESTING");

        assertNull(result);
    }

    @Test
    void testGetAllPayments(){

        // returns list of payments
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(orders.getFirst().getId(), PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), new HashMap<>()));

        doReturn(payments.iterator()).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(payments.getFirst().getId(), result.getFirst().getId());
    }
}
