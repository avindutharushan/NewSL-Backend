package lk.ijse.newslbackend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ADMIN"),
    REPORTER("REPORTER"),
    USER("USER"),
    PREMIUM_USER("PREMIUM_USER");
    private final String roleByString;
}
