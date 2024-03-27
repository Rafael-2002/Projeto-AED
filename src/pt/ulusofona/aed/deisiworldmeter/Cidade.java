package pt.ulusofona.aed.deisiworldmeter;

public class Cidade {
    String alfa2;
    String cidade;

    String  regiao;
    Double populacao;
    Double latitude;
    Double longitude;

    public Cidade(String alfa2, String cidade, String regiao, Double populacao, Double latitude, Double longitude) {

        this.alfa2 = alfa2;
        this.cidade = cidade;
        this.regiao = regiao;
        this.populacao = populacao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Cidade(String alfa2, String cidade, String regiao, Double latitude, Double longitude) {
        this.alfa2 = alfa2;
        this.cidade = cidade;
        this.regiao = regiao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return cidade + " | " + alfa2.toUpperCase() + " | " + regiao + " | "+ populacao.intValue() + " | (" + latitude + "," + longitude+")";
    }
}
