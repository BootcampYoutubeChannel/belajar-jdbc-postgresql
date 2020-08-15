package com.maryanto.dimas.example.entity.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NasabahImage {

    public NasabahImage(Perorangan nasabah, String filename, InputStream file, String createdBy, Timestamp createdDate) {
        this.nasabah = nasabah;
        this.filename = filename;
        this.file = file;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    private String id;
    private Perorangan nasabah;
    private String filename;
    private InputStream file;
    private String createdBy;
    private Timestamp createdDate;
}
