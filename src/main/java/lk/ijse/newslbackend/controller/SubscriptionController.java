package lk.ijse.newslbackend.controller;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lk.ijse.newslbackend.config.PayHereConfig;
import lk.ijse.newslbackend.dto.SubscriptionDTO;
import lk.ijse.newslbackend.entity.enums.SubscriptionTier;
import lk.ijse.newslbackend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class SubscriptionController {
    private final PayHereConfig payHereConfig;
    private SubscriptionService subscriptionService;
    @Value("${payhere.merchant.secret}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }


   /* @GetMapping(path = "/get")
    public ResponseUtil getAllPayments() {
        List<PaymentDTO> payments = subscriptionService.getAllPayments();
        return new ResponseUtil(200, "Payments retrieved successfully", payments);
    }


    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deletePayment(@PathVariable String id) {
        subscriptionService.delete(id);
        return new ResponseUtil(201, "Payment Deleted Successfully", null);
    }

    @PutMapping(path = "/update")
    public ResponseUtil updatePayment(@RequestBody PaymentDTO paymentDTO) {
        subscriptionService.update(paymentDTO);
        return new ResponseUtil(201, "Payment Updated Successfully", null);
    }*/

    @PostMapping(path = "/confirm")
    public ResponseEntity confirmPayment(@RequestBody SubscriptionDTO subscriptionDTO) {
            subscriptionService.save(subscriptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/notify")
    public ResponseEntity<String> handlePaymentNotification(@RequestParam Map<String, String> params) {

        try {
            String merchantId = params.get("merchant_id");
            String orderId = params.get("order_id");
            String payhereAmount = params.get("payhere_amount");
            String payhereCurrency = params.get("payhere_currency");
            String statusCode = params.get("status_code");
            String md5sig = params.get("md5sig");

            // Validate required parameters
            if (merchantId == null || orderId == null || payhereAmount == null ||
                    payhereCurrency == null || statusCode == null || md5sig == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment verification failed: Missing required parameters");
            }

            // Validate amount
            double amount;
            try {
                amount = Double.parseDouble(payhereAmount);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment verification failed: Invalid amount format");
            }

            // Generate the local MD5 signature
            String localMd5sig = generateMd5Sig(
                    merchantId, orderId, payhereAmount, payhereCurrency, statusCode, payHereConfig.getMerchantSecret()
            );

            // Verify the MD5 signature
            if (!localMd5sig.equals(md5sig)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment verification failed: Invalid MD5 signature");
            }

            // Check the payment status
            if ("2".equals(statusCode)) {
                // Payment successful
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                subscriptionDTO.setAmount(amount);
                subscriptionDTO.setStartDate(LocalDate.now());
                subscriptionDTO.setTier(SubscriptionTier.PREMIUM);
                subscriptionService.save(subscriptionDTO);

                return ResponseEntity.ok("Payment verified and processed successfully");
            } else {
                // Payment declined or failed
                String statusMessage = params.get("status_message");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Payment declined: " + statusMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment notification failed: " + e.getMessage());
        }
    }

    private String generateMd5Sig(String merchantId, String orderId, String amount, String currency, String statusCode, String merchantSecret) {
        // Validate input parameters
        if (merchantId == null || orderId == null || amount == null || currency == null || statusCode == null || merchantSecret == null) {
            throw new IllegalArgumentException("One or more required parameters are null");
        }

        // Generate MD5 signature
        String rawData = merchantId + orderId + amount + currency + statusCode + merchantSecret;
        return DigestUtils.md5DigestAsHex(rawData.getBytes()).toUpperCase();
    }

    private String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}