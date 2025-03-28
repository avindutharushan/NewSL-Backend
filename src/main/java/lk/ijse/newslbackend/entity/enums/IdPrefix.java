package lk.ijse.newslbackend.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum class represents the prefixes of the IDs of the entities.
 * The prefixes of the IDs of the entities can be one of the following:
 * 1. USR - The prefix of the ID of a user
 */
@Getter
@RequiredArgsConstructor
public enum IdPrefix {
    ARTICLE("ART");
    private final String prefix;
}