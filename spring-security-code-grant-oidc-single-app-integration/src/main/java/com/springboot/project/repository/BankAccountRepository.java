package com.springboot.project.repository;

import com.springboot.project.entity.BankAccountEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository
        extends JpaRepository<BankAccountEntity, UUID>,
                JpaSpecificationExecutor<BankAccountEntity> {}
