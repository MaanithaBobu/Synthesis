package com.synthesis.hiring.test.dao;

import com.synthesis.hiring.test.model.ContactInformation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInformation, UUID> {

}
