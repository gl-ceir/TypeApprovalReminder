package com.gl.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_param", catalog = "app")
public class SysParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String tag;
    public String value;

    @Column(name = "feature_name")
    public String featureName;

}
