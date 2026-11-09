package com.williamhsieh.financeapp.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

import com.williamhsieh.financeapp.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUserNumber(String userNumber);

    boolean existsByPhone(String phone);

    Optional<User> findByEmailIgnoreCaseAndDeletedDateIsNull(String email);

    Optional<User> findByPhoneAndDeletedDateIsNull(String phone);

    Optional<User> findByIdAndDeletedDateIsNull(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT appUser
        FROM User appUser
        WHERE appUser.id = :userId
        AND appUser.deletedDate IS NULL
        """
    )
    Optional<User>
        findByIdAndDeletedDateIsNullForUpdate(
            @Param("userId") Long userId
        );

    @Query(
        value = """
            SELECT appUser
            FROM User appUser
            WHERE appUser.deletedDate IS NULL
              AND (
                    :statusCode IS NULL
                    OR appUser.accountStatus.code =
                       :statusCode
              )
              AND (
                    :keyword IS NULL
                    OR LOWER(appUser.userNumber)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.name)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.nickname)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.email)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
              )
            ORDER BY
                appUser.createdDate DESC,
                appUser.id DESC
            """,
        countQuery = """
            SELECT COUNT(appUser)
            FROM User appUser
            WHERE appUser.deletedDate IS NULL
              AND (
                    :statusCode IS NULL
                    OR appUser.accountStatus.code =
                       :statusCode
              )
              AND (
                    :keyword IS NULL
                    OR LOWER(appUser.userNumber)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.name)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.nickname)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.email)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
              )
            """
    )
    Page<User> searchUsers(
        @Param("keyword") String keyword,
        @Param("statusCode") String statusCode,
        Pageable pageable
    );

    @Query(
        value = """
            SELECT appUser
            FROM User appUser
            WHERE appUser.deletedDate IS NOT NULL
            AND appUser.accountStatus.code = 'DELETED'
            AND (
                    :keyword IS NULL
                    OR LOWER(appUser.userNumber)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.name)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.nickname)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.email)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
            )
            ORDER BY
                appUser.deletedDate DESC,
                appUser.id DESC
            """,
        countQuery = """
            SELECT COUNT(appUser)
            FROM User appUser
            WHERE appUser.deletedDate IS NOT NULL
            AND appUser.accountStatus.code = 'DELETED'
            AND (
                    :keyword IS NULL
                    OR LOWER(appUser.userNumber)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.name)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.nickname)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
                    OR LOWER(appUser.email)
                        LIKE LOWER(
                            CONCAT('%', :keyword, '%')
                        )
                        ESCAPE '!'
            )
            """
    )
    Page<User> searchDeletedUsers(
        @Param("keyword") String keyword,
        Pageable pageable
    );

    boolean
        existsByIdAndActiveTrueAndDeletedDateIsNullAndAccountStatus_Code(
            Long id,
            String accountStatusCode
        );

    Optional<User>
        findByIdAndDeletedDateIsNotNullAndAccountStatus_Code(
            Long id,
            String accountStatusCode
        );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT appUser
        FROM User appUser
        WHERE appUser.id = :userId
        AND appUser.deletedDate IS NOT NULL
        AND appUser.accountStatus.code =
            :statusCode
        """
    )
    Optional<User> findDeletedUserByIdForUpdate(
        @Param("userId") Long userId,
        @Param("statusCode") String statusCode
    );
}
