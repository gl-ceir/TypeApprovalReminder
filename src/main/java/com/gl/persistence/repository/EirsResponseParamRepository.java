package com.gl.persistence.repository;

import com.gl.persistence.entities.EirsResponseParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EirsResponseParamRepository extends JpaRepository<EirsResponseParam, Long> {
    EirsResponseParam findByTagAndLanguage(String tag, String language);
}
