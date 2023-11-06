package com.iso.spring3.services.user;

import com.iso.spring3.core.utils.business.BusinessRule;
import com.iso.spring3.core.utils.constants.CoreConstants;
import com.iso.spring3.core.utils.result.*;
import com.iso.spring3.security.models.RoleType;
import com.iso.spring3.security.models.User;
import com.iso.spring3.security.repo.RoleRepository;
import com.iso.spring3.security.repo.UserRepository;
import com.kim.dibt.core.utils.result.*;
import com.iso.spring3.services.ServiceMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository repository;

    @Override
    public DataResult<User> getUserById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ErrorDataResult.of(null, ServiceMessages.USER_NOT_FOUND);
        }
        return SuccessDataResult.of(user, ServiceMessages.USER_FOUND);
    }


    @Override
    public Result addRoleToUser(Long userId, RoleType roleType) {
        var result = BusinessRule.run(
                checkRoleExists(roleType),
                checkUserExists(userId)
        );
        if (result != null) {
            return result;
        }
        if (checkUserExistsByIdAndRoleName(userId, roleType)) {
            return ErrorResult.of(CoreConstants.USER_ALREADY_HAS_ROLE);
        }
        User user = userRepository.findById(userId).orElse(null);
        var role = repository.findByRoleName(roleType).orElse(null);
        assert user != null;
        user.getRoles().add(role);
        userRepository.save(user);
        return SuccessResult.of(ServiceMessages.ROLE_ADDED_TO_USER);
    }

    private Result checkRoleExists(RoleType roleType) {
        return repository.existsByRoleName(roleType) ? SuccessResult.of() : ErrorResult.of(CoreConstants.ROLE_NOT_FOUND);
    }

    private Result checkUserExists(Long userId) {
        return repository.existsById(userId) ? SuccessResult.of() : ErrorResult.of(CoreConstants.USER_NOT_FOUND);
    }

    private boolean checkUserExistsByIdAndRoleName(Long userId, RoleType roleType) {
        var user = userRepository.findById(userId).orElse(null);
        assert user != null;
        for (var role : user.getRoles()) {
            if (role.getRoleName().equals(roleType)) {
                return true;
            }
        }
        return false;
    }

}
