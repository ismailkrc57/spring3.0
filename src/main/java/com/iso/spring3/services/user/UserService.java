package com.iso.spring3.services.user;

import com.iso.spring3.core.utils.result.DataResult;
import com.iso.spring3.core.utils.result.Result;
import com.iso.spring3.security.models.RoleType;
import com.iso.spring3.security.models.User;

public interface UserService {
    DataResult<User> getUserById(long id);
    Result addRoleToUser(Long userId, RoleType roleType);
}
