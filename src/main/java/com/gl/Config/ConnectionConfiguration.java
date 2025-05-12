package com.gl.Config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;


import org.springframework.stereotype.Repository;
//

import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ConnectionConfiguration {

    @PersistenceContext
    private EntityManager em;

    public Connection getConnection() {
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) em.getEntityManagerFactory();
        try {
            return Objects.requireNonNull(info.getDataSource()).getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

}