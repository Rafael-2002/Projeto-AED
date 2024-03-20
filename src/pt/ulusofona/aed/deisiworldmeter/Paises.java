package pt.ulusofona.aed.deisiworldmeter;

public class Paises {
    int id;
    String alfa2,alfa3,nome;


    public Paises(int id, String alfa2, String alfa3, String nome) {
        this.id = id;
        this.alfa2 = alfa2;
        this.alfa3 = alfa3;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "" + id + "," + alfa2 + "," + alfa3 + "," + nome;
    }
}

