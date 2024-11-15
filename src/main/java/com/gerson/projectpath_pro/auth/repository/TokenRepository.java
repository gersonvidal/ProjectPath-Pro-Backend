package com.gerson.projectpath_pro.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
        select t from Token t inner join User u\s
        on t.user.id = u.id\s
        where u.id = :id and (t.isExpired = false or t.isRevoked = false)\s
        """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);

}
