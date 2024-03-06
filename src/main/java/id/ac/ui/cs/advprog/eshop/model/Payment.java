package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    Map<String, String> paymentData;
    @Setter
    String status;

    public Payment(String id, String method, String status, Map<String, String> paymentData){
        this.id = id;
        this.paymentData = paymentData;

        this.setMethod(method);
        this.setStatus(status);
    }

    private void setMethod(String param){
        if (PaymentMethod.contains(param)){
            this.method = param;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void validatePendingStatus(){
        if (this.status != null && !this.status.equals(PaymentStatus.PENDING.getValue())){
            throw new IllegalStateException();
        }
    }

    public void setStatus(String param){
        validatePendingStatus();

        if (PaymentStatus.contains(param)) {
            this.status = param;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
