package com.kimoota.kimootaapiservice.repository.user;

import com.kimoota.kimootaapiservice.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    List<User> findByName(String name);
    List<User> findAll();

    void deleteById(Long id);

}
