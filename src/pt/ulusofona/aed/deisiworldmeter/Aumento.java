package pt.ulusofona.aed.deisiworldmeter;

import java.text.DecimalFormat;

public class Aumento {
    String pais;

    String anos;

    Double percentagem;

    public Aumento(String pais, String anos, Double percentagem) {
        this.pais = pais;
        this.anos = anos;
        this.percentagem =percentagem;
    }
}
