package com.williamhsieh.financeapp.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.williamhsieh.financeapp.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUserNumber(String userNumber);

    boolean existsByPhone(String phone);

    Optional<User> findByEmailIgnoreCaseAndDeletedDateIsNull(String email);

    Optional<User> findByPhoneAndDeletedDateIsNull(String phone);

    Optional<User> findByIdAndDeletedDateIsNull(Long id);

    @Query(
        value = """
            SELECT appUser
            FROM User appUser
            WHERE appUser.deletedDate IS NULL
            ORDER BY
                appUser.createdDate DESC,
                appUser.id DESC
            """,
        countQuery = """
            SELECT COUNT(appUser)
            FROM User appUser
            WHERE appUser.deletedDate IS NULL
            """
    )
    Page<User> findAllByDeletedDateIsNull(Pageable pageable);
}
