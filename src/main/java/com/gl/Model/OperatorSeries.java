package com.gl.Model;


import org.springframework.stereotype.Component;

@Component
public class OperatorSeries {

    public Integer series_start, series_end;

    public String series_type, operator_name;

    public OperatorSeries(){}

    public OperatorSeries(Integer series_start, Integer series_end, String series_type, String operator_name) {
        this.series_start = series_start;
        this.series_end = series_end;
        this.series_type = series_type;
        this.operator_name = operator_name;
    }

}
