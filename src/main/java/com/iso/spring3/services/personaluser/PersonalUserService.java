package com.iso.spring3.services.personaluser;

import com.iso.spring3.core.utils.result.DataResult;
import com.iso.spring3.core.utils.result.Result;
import com.iso.spring3.services.personaluser.dtos.AddPersonalUser;
import com.iso.spring3.services.personaluser.dtos.AddedPersonalUser;
import com.iso.spring3.services.personaluser.dtos.UpdatePersonalUser;
import jakarta.servlet.http.HttpServletRequest;

public interface PersonalUserService {
    DataResult<AddedPersonalUser> addPersonalUser(AddPersonalUser addPersonalUser, HttpServletRequest request);

    Result updatePersonalUser(UpdatePersonalUser updatePersonalUser, long userId, HttpServletRequest request);
}
