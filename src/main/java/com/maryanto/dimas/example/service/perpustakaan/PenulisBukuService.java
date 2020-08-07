package com.maryanto.dimas.example.service.perpustakaan;

import com.maryanto.dimas.example.dao.perpustakaan.BukuDao;
import com.maryanto.dimas.example.dao.perpustakaan.PenulisBukuDao;
import com.maryanto.dimas.example.dao.perpustakaan.PenulisDao;
import com.maryanto.dimas.example.entity.perpustakaan.Buku;
import com.maryanto.dimas.example.entity.perpustakaan.Penulis;
import com.maryanto.dimas.example.entity.perpustakaan.PenulisBuku;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PenulisBukuService {

    private final BukuDao bukuDao;
    private final PenulisDao penulisDao;
    private final PenulisBukuDao penulisBukuDao;

    public PenulisBukuService(BukuDao bukuDao, PenulisDao penulisDao, PenulisBukuDao penulisBukuDao) {
        this.bukuDao = bukuDao;
        this.penulisDao = penulisDao;
        this.penulisBukuDao = penulisBukuDao;
    }

    public Buku saveByBuku(Buku buku, Penulis penulis) throws SQLException {
        Optional<Buku> bukuOptional = bukuDao.findById(buku.getId());
        if (!bukuOptional.isPresent())
            buku = this.bukuDao.save(buku);

        Optional<Penulis> penulisOptional = penulisDao.findById(penulis.getId());
        if (!penulisOptional.isPresent())
            penulis = this.penulisDao.save(penulis);

        this.penulisBukuDao.save(new PenulisBuku(null, buku, penulis));
        return buku;
    }

    public Penulis saveByPenulis(Penulis penulis, Buku... listBuku) throws SQLException {
        Optional<Penulis> penulisOptional = penulisDao.findById(penulis.getId());
        if (!penulisOptional.isPresent())
            penulis = this.penulisDao.save(penulis);


        List<Buku> list = Arrays.asList(listBuku.clone());
        List<PenulisBuku> listPenulisBuku = new ArrayList<>();
        for (Buku buku : list) {
            Optional<Buku> bukuOptional = bukuDao.findById(buku.getId());
            if (!bukuOptional.isPresent()) {
                buku = this.bukuDao.save(buku);
                listPenulisBuku.add(new PenulisBuku(null, buku, penulis));
            } else {
                listPenulisBuku.add(new PenulisBuku(null, buku, penulis));
            }
        }

        this.penulisBukuDao.save(listPenulisBuku);
        return penulis;
    }
}
