package pt.ulusofona.aed.deisiworldmeter;

public class Populacao {
    int id;
    int ano;
    Long popMasculina;
    Long popFeminina;
    Double densidade;

    public Populacao(int id, int ano, Long popMasculina, Long popFeminina, Double densidade) {
        this.id = id;
        this.ano = ano;
        this.popMasculina = popMasculina;
        this.popFeminina = popFeminina;
        this.densidade = densidade;
    }

    public Populacao(int id, Long popMasculina, Long popFeminina, Double densidade) {
        this.id = id;
        this.popMasculina = popMasculina;
        this.popFeminina = popFeminina;
        this.densidade = densidade;
    }

    @Override
    public String toString() {
        return id + " | " + popMasculina + " | " + popFeminina + " | " + densidade;
    }


}
