package lk.ijse.newslbackend.service;

import lk.ijse.newslbackend.dto.SubscriptionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubscriptionService {
    void save(SubscriptionDTO paymentDTO);
    List<SubscriptionDTO> getAll();
    void delete(String id);
    void update(SubscriptionDTO paymentDTO);

    List<SubscriptionDTO> getAllPayments();
}
