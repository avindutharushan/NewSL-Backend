package lk.ijse.newslbackend.dto;

import jakarta.persistence.*;
import lk.ijse.newslbackend.entity.User;
import lk.ijse.newslbackend.entity.enums.PaymentStatus;
import lk.ijse.newslbackend.entity.enums.SubscriptionTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionDTO {
    private String user;
    private SubscriptionTier tier;
    private LocalDate startDate;
    private double amount;
}
