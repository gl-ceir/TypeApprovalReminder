package com.gl.persistence.repository;

import com.gl.persistence.entities.SysParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysParamRepository extends JpaRepository<SysParam, Long> {

    public SysParam findByTag(String tag);

}
