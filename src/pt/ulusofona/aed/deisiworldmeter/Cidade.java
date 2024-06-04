package pt.ulusofona.aed.deisiworldmeter;

public class Cidade {
    String alfa2;
    String cidade;

    String  regiao;
    Double populacao;
    Double latitude ;
    Double longitude;

    public Cidade(String alfa2, String cidade, String regiao, Double populacao, Double latitude, Double longitude) {

        this.alfa2 = alfa2;
        this.cidade = cidade;
        this.regiao = regiao;
        this.populacao = populacao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Cidade(String alfa2, String cidade, String regiao,Double populacao) {
        this.alfa2 = alfa2;
        this.cidade = cidade;
        this.regiao = regiao;
        this.populacao = populacao;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    @Override
    public String toString() {
        return cidade + " | " + alfa2.toUpperCase() + " | " + regiao + " | "+ populacao.intValue() + " | (" + latitude + "," + longitude+")";
    }


    public Double getPopulacao() {
        return populacao;
    }

    public String getFormattedPopulacao() {
        return (int) (populacao / 1000) + "K";
    }
}
