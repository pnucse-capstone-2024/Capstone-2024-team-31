package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.Email;
import org.springframework.data.repository.CrudRepository;

public interface MailRepository extends CrudRepository<Email, String> {

}
