package com.maryanto.dimas.example.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"listBuku"})
public class Penerbit {

    private String id;
    private String nama;
    private String alamat;
    private List<Buku> listBuku = new ArrayList<>();
}
