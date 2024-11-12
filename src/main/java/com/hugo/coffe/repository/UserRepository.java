package com.hugo.coffe.repository;

import com.hugo.coffe.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    Integer updateStatus(@Param("id") Integer id, @Param("status") String status);

    //lista de usuarios admin por su email
    @Query("select u.email from User u where u.role='admin'")
    List<String> getAllAdmin();
}
