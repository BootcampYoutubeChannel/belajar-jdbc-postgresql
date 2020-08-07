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
@ToString(exclude = {"listPenulis"})
public class Buku {

    public Buku(String id) {
        this.id = id;
    }

    private String id;
    private String nama;
    private String isbn;
    private Penerbit penerbit;
    private List<Penulis> listPenulis = new ArrayList<>();

}
