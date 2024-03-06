package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = paymentRepository.findById(order.getId());
        if (payment == null && OrderStatus.WAITING_PAYMENT.getValue().equals(order.getStatus())) {
            if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)){
                return addPaymentVoucherCode(order, paymentData);

            } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)) {
                return addPaymentBankTransfer(order, paymentData);
            }
        }

        return null;
    }

    private Payment addPaymentVoucherCode(Order order, Map<String, String> paymentData){
        String voucherCode = paymentData.get("voucherCode");

        if (isValidVoucherCode(voucherCode)){
            return setPaymentSuccess(order, PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        } else {
            return setPaymentRejected(order, PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        }
    }

    private Payment addPaymentBankTransfer(Order order, Map<String, String> paymentData){
        if (stringNotNullOrEmpty(paymentData.get("bankName"))
                && stringNotNullOrEmpty(paymentData.get("referenceCode"))){
            return setPaymentPending(order, PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        } else {
            return setPaymentRejected(order, PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        }
    }

    private Payment setPaymentPending(Order order, String method, Map<String, String> paymentData){
        Payment payment = new Payment(order.getId(), method,
                PaymentStatus.PENDING.getValue(), paymentData);

        paymentRepository.save(payment);
        return payment;
    }

    private Payment setPaymentSuccess(Order order, String method, Map<String, String> paymentData){
        Payment payment = new Payment(order.getId(), method,
                PaymentStatus.SUCCESS.getValue(), paymentData);

        paymentRepository.save(payment);

        orderService.updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
        return payment;
    }

    private Payment setPaymentRejected(Order order, String method, Map<String, String> paymentData){
        Payment payment = new Payment(order.getId(), method,
                PaymentStatus.REJECTED.getValue(), paymentData);

        paymentRepository.save(payment);

        orderService.updateStatus(order.getId(), OrderStatus.FAILED.getValue());
        return payment;
    }

    private boolean isValidVoucherCode(String voucherCode){
        return voucherCode != null
                && voucherCode.length() == 16
                && voucherCode.startsWith("ESHOP")
                && voucherCode.replaceAll("[^0-9]","").length() == 8;
    }

    private boolean stringNotNullOrEmpty(String string){
        return string != null && !string.isBlank();
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        Order order = orderService.findById(payment.getId());
        Payment savedPayment = paymentRepository.findById(payment.getId());
        if (order != null && savedPayment != null && OrderStatus.WAITING_PAYMENT.getValue().equals(order.getStatus())){
            if (PaymentStatus.SUCCESS.getValue().equals(status)){
                return setPaymentSuccess(order, payment.getMethod(), payment.getPaymentData());
            } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
                return setPaymentRejected(order, payment.getMethod(), payment.getPaymentData());
            }
        }
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        Iterator<Payment> paymentIterator = paymentRepository.findAll();
        List<Payment> allPayment = new ArrayList<>();
        paymentIterator.forEachRemaining(allPayment::add);
        return allPayment;
    }
}
