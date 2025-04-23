package lk.ijse.newslbackend.repository;

import lk.ijse.newslbackend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
}
