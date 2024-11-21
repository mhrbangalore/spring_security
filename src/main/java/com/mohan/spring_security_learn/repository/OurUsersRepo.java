package com.mohan.spring_security_learn.repository;

import com.mohan.spring_security_learn.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OurUsersRepo extends JpaRepository<OurUsers, Integer> {

    Optional<OurUsers> findByEmail(String email);

}
