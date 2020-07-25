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

    private String id;
    private String nama;
    private String isbn;
    private Penerbit penerbit;
    private List<Penulis> listPenulis = new ArrayList<>();

}
