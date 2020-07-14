package com.maryanto.dimas.example.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"details"})
public class Transaksi {

    private String id;
    private Date createdDate;
    private Anggota anggota;
    private List<TransactionDetail> details = new ArrayList<>();
}
