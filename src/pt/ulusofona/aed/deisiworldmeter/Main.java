package pt.ulusofona.aed.deisiworldmeter;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
//aa
    static ArrayList<Cidade> infoCidades;
    static ArrayList<Paises> infoPaises;
    static ArrayList<Populacao> infoPopulacao;

    public static ArrayList<Object> getObjects(TipoEntidade tipo) {

        ArrayList<Integer> intArrayList = new ArrayList<>();

        int[] ArrayCidade = {};
        int[] ArrayPais = {};
        int[] ArrayPopulacao = {};

        int linhasErradasCidades = 0;
        int linhasCertasCidades = 0;
        int linhasdoErroCidades = -1;


        if (tipo == TipoEntidade.PAIS){

        }

        if (tipo == TipoEntidade.CIDADE){

        }

        if (tipo == TipoEntidade.INPUT_INVALIDO){

            /*for(int i = 0; i <= infoCidades.size(); i++){

                String[] elementos = infoCidades.get(i).toString().split(",");

                if(elementos.length != 6){
                    linhasErradasCidades++;
                }else{
                    linhasCertasCidades++;
                }
            }*/

        }




        return new ArrayList<>();
    }

    public static boolean parseFiles(File pasta){
        File filePaises = new File(pasta, "paises.csv");
        File filePopulacao = new File(pasta, "populacao.csv");
        File fileCidades = new File(pasta, "cidades.csv");

        infoCidades = new ArrayList<>();
        infoPaises = new ArrayList<>();
        infoPopulacao = new ArrayList<>();

        // Lê o arquivo de países
        try (BufferedReader reader = new BufferedReader(new FileReader(filePaises))) {
            String linha;
            // Ignora a primeira linha (títulos das colunas)
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 4) {
                    int id = Integer.parseInt(dados[0].trim());
                    String alfa2 = dados[1];
                    String alfa3 = dados[2];
                    String nome = dados[3];
                    Paises pais = new Paises(id, alfa2, alfa3, nome);
                    infoPaises.add(pais);
                }
            }
        }catch (IOException e) {
            return false;
        }


        // Lê o arquivo de população
        try (BufferedReader reader = new BufferedReader(new FileReader(filePopulacao))) {
            String linha;
            // Ignora a primeira linha (títulos das colunas)
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 5) {
                    int id = Integer.parseInt(dados[0].trim());
                    int ano = Integer.parseInt(dados[1].trim());
                    long popMasculina = Long.parseLong(dados[2].trim());
                    long popFeminina = Long.parseLong(dados[3].trim());
                    Double densidade = Double.parseDouble(dados[4].trim());

                    Populacao populacao = new Populacao(id, ano, popMasculina, popFeminina, densidade);
                    infoPopulacao.add(populacao);
                }
            }
        }catch (IOException e) {
            return false;
        }


        // Lê o arquivo de cidades
        try (BufferedReader reader = new BufferedReader(new FileReader(fileCidades))) {
            String linha;
            // Ignora a primeira linha (títulos das colunas)
            reader.readLine();
            int lineNumber = 2; // Linha 1 é o cabeçalho
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 6) {
                    String alfa2 = dados[0];
                    String cidade = dados[1];
                    int regiao = Integer.parseInt(dados[2].trim());
                    Double populacao = !dados[3].isEmpty() ? Double.parseDouble(dados[3]) : null;
                    Double latitude = Double.parseDouble(dados[4]);
                    Double longitude = Double.parseDouble(dados[5]);
                    Cidade cidades = new Cidade(alfa2, cidade, regiao, populacao, latitude, longitude);
                    infoCidades.add(cidades);
                }
            }
        }catch (IOException e) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        File pasta = new File(".");
            boolean leuBem = parseFiles(pasta);
            if (leuBem) {
                System.out.println("Leitura dos ficheiros realizada com sucesso!");
                System.out.println(infoPaises.get(0));
                System.out.println(infoCidades.get(0).toString());
                System.out.println(infoPopulacao.get(0).toString());
                // Adicione aqui o código para testar ou manipular os dados lidos
            } else {
                System.out.println("Falha ao ler os ficheiros.");
            }
    }
}