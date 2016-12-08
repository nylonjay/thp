package thp.csii.com.paysdk.exception;

/**
 * @author zhaojin 15398699939@163.com
 * @create 2016-08-15-21:12
 */

public class PaymentSDKException extends RuntimeException{

    public PaymentSDKException() {
    }

    public PaymentSDKException(String message) {
        super(message);
    }

    public PaymentSDKException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentSDKException(Throwable cause) {
        super(cause);
    }
}
