package com.iso.spring3.security.auth;

import com.iso.spring3.core.utils.result.DataResult;
import com.iso.spring3.security.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @GetMapping
    public ResponseEntity<DataResult<List<RoleDto>>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
