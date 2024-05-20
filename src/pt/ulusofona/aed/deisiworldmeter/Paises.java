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
        int countNrMaioresQ700 = 0;

        int tamanho = Main.infoPopulacao.size();

        if(id>700){
            for (int i = 0; i < Main.infoPopulacao.size() ; i++) {
                if(Main.infoPopulacao.get(i).id==this.id){
                    countNrMaioresQ700++;
                }
            }
            return nome + " | " + id + " | " + alfa2.toUpperCase() + " | " + alfa3.toUpperCase()+ " | "+countNrMaioresQ700;
        }else{
            return nome + " | " + id + " | " + alfa2.toUpperCase() + " | " + alfa3.toUpperCase();
        }

    }

    public int getId() {
        return id;
    }
}

