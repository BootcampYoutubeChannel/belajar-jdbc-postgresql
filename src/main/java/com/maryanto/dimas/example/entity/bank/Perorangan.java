package com.maryanto.dimas.example.entity.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Perorangan extends Nasabah {

    private String ktp;
    private String foto;
}
