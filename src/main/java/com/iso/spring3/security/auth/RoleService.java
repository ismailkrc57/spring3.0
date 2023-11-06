package com.iso.spring3.security.auth;


import com.iso.spring3.core.utils.business.CustomModelMapper;
import com.iso.spring3.core.utils.constants.CoreConstants;
import com.iso.spring3.core.utils.result.DataResult;
import com.iso.spring3.core.utils.result.SuccessDataResult;
import com.iso.spring3.security.repo.RoleRepository;
import com.iso.spring3.security.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;
    private final CustomModelMapper modelMapper;

    public DataResult<List<RoleDto>> getAll() {
        List<RoleDto> roleList = modelMapper.mapList(repository.findAll(), RoleDto.class);
        return SuccessDataResult.of(roleList, CoreConstants.LISTED);
    }

}
