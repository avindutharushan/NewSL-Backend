package lk.ijse.newslbackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.newslbackend.customObj.impl.MailBody;
import lk.ijse.newslbackend.dto.SubscriptionDTO;
import lk.ijse.newslbackend.entity.Subscription;
import lk.ijse.newslbackend.entity.User;
import lk.ijse.newslbackend.repository.SubscriptionRepository;
import lk.ijse.newslbackend.repository.UserRepository;
import lk.ijse.newslbackend.service.SubscriptionService;
import lk.ijse.newslbackend.util.EmailUtil;
import lk.ijse.newslbackend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final Mapping mapping;
    private final EmailUtil emailUtil;


    @Override
    @Transactional
    public void save(SubscriptionDTO subscriptionDTO) {
        if (subscriptionDTO.getUser() == null || subscriptionDTO.getUser().isEmpty()) {
            throw new UsernameNotFoundException("you are not logged in");
        }
        subscriptionDTO.setStartDate(LocalDate.now());
        Subscription payment = mapping.convertToEntity(subscriptionDTO, Subscription.class);
        subscriptionRepository.save(payment);

        Optional<User> byUsername = userRepository.findByUsername(subscriptionDTO.getUser());

        Map<String, Object> map = Map.of("name", subscriptionDTO.getUser(),"date",subscriptionDTO.getStartDate());
        emailUtil.sendHtmlMessage(
                MailBody.builder()
                        .to(byUsername.get().getEmail())
                        .subject("Payment Success")
                        .templateName("payment-success")
                        .replacements(map)
                        .build()
        );
    }

    @Override
    public List<SubscriptionDTO> getAll() {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(SubscriptionDTO paymentDTO) {

    }

    @Override
    public List<SubscriptionDTO> getAllPayments() {
        /*List<Subscription> payments = subscriptionRepository.findAll();
        return payments.stream().map(payment -> {
            SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
            subscriptionDTO.setPid(payment.getPid());
            subscriptionDTO.setAmount(payment.getAmount());
            subscriptionDTO.setPaymentDate(payment.getPaymentdate());
            subscriptionDTO.setPaymentMethodId(payment.getPaymentMethodId());
            subscriptionDTO.setBidId(payment.getBidId());
            subscriptionDTO.setSpiceOwnerEmail(payment.getSpiceOwnerEmail());
            subscriptionDTO.setBuyerEmail(payment.getBuyerEmail());
            subscriptionDTO.setFriendlyId("PAY-" + payment.getPid().toString().substring(0, 8).toUpperCase());
            return subscriptionDTO;
        }).collect(Collectors.toList());*/
        return null;
    }
}