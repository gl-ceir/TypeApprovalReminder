package com.gl.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity
@Table(name = "eirs_response_param", catalog = "app")
public class EirsResponseParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    private String value;

    private String language;
    @Column(name = "feature_name")
    public String featureName;
}
