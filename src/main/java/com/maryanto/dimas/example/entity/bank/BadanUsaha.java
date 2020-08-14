package com.maryanto.dimas.example.entity.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BadanUsaha extends Nasabah {

    public BadanUsaha(String cif) {
        setCif(cif);
    }

    private String npwp;
    private String siup;
}


