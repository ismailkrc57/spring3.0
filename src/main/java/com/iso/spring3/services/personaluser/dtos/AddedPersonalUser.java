package com.iso.spring3.services.personaluser.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddedPersonalUser {
    private String firstName;
    private String lastName;
}
