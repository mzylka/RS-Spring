package com.rs.app.mappers;

import com.rs.app.domain.entities.User;
import com.rs.app.dto.user.UserDTO;
import com.rs.app.dto.user.UserSimpleDTO;

public interface UserMapper{
    UserSimpleDTO toSimpleDto(User user);
    UserDTO toDto(User user);
}
