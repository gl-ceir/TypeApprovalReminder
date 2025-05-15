package com.gl.processer.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class QueryExecutorService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Long executeWithKey(String query) {
        long start = System.currentTimeMillis();
        log.info("DB[{}] Going to Execute Query:{}", query);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            return ps;
        }, keyHolder);
        log.info("DB[{}] Executed TimeTaken:{} Result:{} Query:{}", (System.currentTimeMillis() - start), keyHolder.getKey().longValue(), query);
        return keyHolder.getKey().longValue();
    }


    public Long executeAndGetKey(String query) {
        Long generatedKey = 0L;
        try (Connection conn = getJdbcTemplate().getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query, new String[]{"ID"})) {
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getLong(1);
            }
            rs.close();
        } catch (Exception e) {
            log.error("Not able to   Query:{} , {} ", query, e.getCause().getMessage(), e);
        }
        return generatedKey;
    }


    public Integer execute(String query) {
        long start = System.currentTimeMillis();
        log.info("DB[{}] Going to Execute Query:{}", query);
        Integer result = jdbcTemplate.update(query);
        log.info("DB[{}] Executed TimeTaken:{} Result:{} Query:{}", (System.currentTimeMillis() - start), result, query);
        return result;
    }

    public Integer executeCreate(String query) {
        long start = System.currentTimeMillis();
        try {
            log.info("DB[{}] Going to Execute Query:{}", query);
            Integer result = jdbcTemplate.update(query);
            log.info("DB[{}] Executed TimeTaken:{} Result:{} Query:{}", (System.currentTimeMillis() - start), result, query);
            return result;
        } catch (Exception e) {
            log.error("Error while executing TimeTaken:{} Query:{} Error:{}", (System.currentTimeMillis() - start), query, e.getCause().getMessage());
            return -1;
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}