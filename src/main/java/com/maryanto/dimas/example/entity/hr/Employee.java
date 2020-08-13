package com.maryanto.dimas.example.entity.hr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private String id;
    private String nama;
    private Date tanggalLahir;
    private Date tanggalJoin;
    private BigDecimal salary;
    private Job job;
    private Employee manager;
}
