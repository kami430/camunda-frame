package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.dataInterface.entity.Attachement;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachementDao extends BaseJpaRepository<Attachement,Long> {
}
