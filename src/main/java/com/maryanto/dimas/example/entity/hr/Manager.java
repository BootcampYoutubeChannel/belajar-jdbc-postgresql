package com.maryanto.dimas.example.entity.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"children"})
public class Manager {

    private String id;
    private String nama;
    private Date tanggalLahir;
    private Date tanggalJoin;
    private BigDecimal salary;
    private Job job;
    private List<Manager> children = new ArrayList<>();
}
