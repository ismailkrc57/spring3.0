package com.iso.spring3.services.personaluser;

import com.iso.spring3.core.utils.business.BusinessRule;
import com.iso.spring3.core.utils.business.CustomModelMapper;
import com.iso.spring3.core.utils.result.*;
import com.iso.spring3.models.PersonalUser;
import com.iso.spring3.repo.PersonalUserRepo;
import com.iso.spring3.security.config.JwtService;
import com.iso.spring3.services.personaluser.dtos.AddPersonalUser;
import com.iso.spring3.services.personaluser.dtos.AddedPersonalUser;
import com.iso.spring3.services.personaluser.dtos.UpdatePersonalUser;
import com.iso.spring3.services.personaluser.dtos.UpdatedPersonalUser;
import com.kim.dibt.core.utils.result.*;
import com.iso.spring3.services.ServiceMessages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalUserManager implements PersonalUserService {
    private final CustomModelMapper modelMapper;
    private final PersonalUserRepo personalUserRepo;
    private final JwtService jwtService;

    @Override
    public DataResult<AddedPersonalUser> addPersonalUser(AddPersonalUser addPersonalUser, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        PersonalUser personalUser = findUserByToken(token);
        if (personalUser == null) {
            return ErrorDataResult.of(null, ServiceMessages.USER_NOT_FOUND);
        }
        var result = BusinessRule.run(checkNationalIdExists(addPersonalUser.getNationalityId(), personalUser.getUsername()));
        if (result != null) {
            return ErrorDataResult.of(null, result.getMessage());
        }
        modelMapper.ofStrict().map(addPersonalUser, personalUser);
        PersonalUser savedPersonalUser = personalUserRepo.save(personalUser);
        AddedPersonalUser addedPersonalUser = modelMapper.ofStandard().map(savedPersonalUser, AddedPersonalUser.class);
        return SuccessDataResult.of(addedPersonalUser, ServiceMessages.PERSONAL_USER_ADDED);

    }

    @Override
    public Result updatePersonalUser(
            UpdatePersonalUser updatePersonalUser,
            long userId,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        Optional<PersonalUser> personalUser = personalUserRepo.findByUsername(username);
        if (personalUser.isEmpty() || personalUser.get().getId() != userId) {
            return ErrorDataResult.of(null, ServiceMessages.USER_NOT_FOUND);
        }
        modelMapper.ofStrict().map(updatePersonalUser, personalUser.get());
        log.debug("personalUser: {}", personalUser.get());
        PersonalUser user = personalUserRepo.save(personalUser.get());
        UpdatedPersonalUser updatedPersonalUser = modelMapper.ofStandard().map(user, UpdatedPersonalUser.class);
        return SuccessDataResult.of(updatedPersonalUser, ServiceMessages.PERSONAL_USER_UPDATED);


    }

    // check if user exists by national id but not by user username
    private Result checkNationalIdExists(String nationalId, String username) {
        Optional<PersonalUser> byNationalityId = personalUserRepo.findByNationalityId(nationalId);
        if (byNationalityId.isPresent()) {
            if (byNationalityId.get().getUsername().equals(username)) {
                return SuccessResult.of();
            }
            return ErrorResult.of(ServiceMessages.NATIONAL_ID_ALREADY_TAKEN);
        }
        return SuccessResult.of();
    }

    private PersonalUser findUserByToken(String token) {
        token = token.substring(7);
        String username = jwtService.extractUsername(token);
        if (username == null) {
            return null;
        }
        Optional<PersonalUser> personalUser = personalUserRepo.findByUsername(username);
        return personalUser.orElse(null);
    }


}
