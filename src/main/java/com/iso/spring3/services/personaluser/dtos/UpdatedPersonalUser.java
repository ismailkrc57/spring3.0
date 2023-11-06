package com.iso.spring3.services.personaluser.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatedPersonalUser {
    private String firstName;
    private String lastName;
    private String nationalityId;
    private String email;
    private String username;
}
