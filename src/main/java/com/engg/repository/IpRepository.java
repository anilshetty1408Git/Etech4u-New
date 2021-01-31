package com.engg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.engg.model.IpAddress;

@Repository
@Transactional
public interface IpRepository extends JpaRepository<IpAddress, Long>, JpaSpecificationExecutor<IpAddress> {

}
