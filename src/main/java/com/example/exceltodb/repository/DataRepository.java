package com.example.exceltodb.repository;

import com.example.exceltodb.entity.DataToSaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<DataToSaveEntity, Long> {
}