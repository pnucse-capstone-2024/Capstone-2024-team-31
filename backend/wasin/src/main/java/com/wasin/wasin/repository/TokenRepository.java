package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {

    Optional<Token> findByAccessToken(String accessToken);

}
