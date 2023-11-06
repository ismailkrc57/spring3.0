package com.iso.spring3.models;

import com.iso.spring3.security.models.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "personal_users")
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class PersonalUser extends User {
    private String firstName;
    private String lastName;
    private String nationalityId;
    private String birthDate;

}
