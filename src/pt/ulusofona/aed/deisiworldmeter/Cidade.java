package pt.ulusofona.aed.deisiworldmeter;

public class Cidade {
    String alfa2;
    String cidade;
    int  regiao;
    Double populacao;
    Double latitude;
    Double longitude;

    public Cidade(String alfa2, String cidade, int regiao, Double populacao, Double latitude, Double longitude) {

        this.alfa2 = alfa2;
        this.cidade = cidade;
        this.regiao = regiao;
        this.populacao = populacao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Cidade(String alfa2, String cidade, int regiao, Double latitude, Double longitude) {
        this.alfa2 = alfa2;
        this.cidade = cidade;
        this.regiao = regiao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return alfa2 + "," + cidade + "," + regiao + "," + latitude + "," + longitude;
    }
}
