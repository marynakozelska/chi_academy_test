package com.example.chi_academy_test.repository;

import com.example.chi_academy_test.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);

    boolean existsByLogin(String login);

    void deleteUserByLogin(String login);
}
