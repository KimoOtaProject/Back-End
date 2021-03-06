package com.kimoota.kimootaapiservice.repository.user;

import com.kimoota.kimootaapiservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaUserRepository extends JpaRepository<User,Long>, UserRepository {

    @Override
    @Query("from User u where u.enabled = true and u.id = :id")
    Optional<User> findById(@Param("id")Long id);

    @Override
    @Query("from User u where u.enabled = true and u.email = :email")
    Optional<User> findByEmail(@Param("email")String email);

    @Override
    @Query("from User u where u.enabled = true and u.name = :name")
    List<User> findByName(@Param("name")String name);

    @Override
    @Transactional
    @Modifying
    @Query("update User u set u.enabled = false where u.id=:id")
    void deleteById(@Param("id")Long id);
}
