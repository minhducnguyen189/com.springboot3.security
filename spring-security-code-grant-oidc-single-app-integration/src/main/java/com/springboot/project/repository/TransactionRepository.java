package com.springboot.project.repository;

import com.springboot.project.entity.TransactionDetailEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<TransactionDetailEntity, UUID>,
                JpaSpecificationExecutor<TransactionDetailEntity> {}
