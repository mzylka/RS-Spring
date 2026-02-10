package com.rs.app.dto;

import com.rs.app.domain.enums.PublicationStatus;

public interface Authorable {
    PublicationStatus status();
    Long authorId();
}
