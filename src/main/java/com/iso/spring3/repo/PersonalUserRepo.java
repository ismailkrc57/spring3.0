package com.iso.spring3.repo;

import com.iso.spring3.models.PersonalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalUserRepo extends JpaRepository<PersonalUser, Long> {

    Optional<PersonalUser> findByUsername(String username);

    Optional<PersonalUser> findByNationalityId(String nationalityId);
}
