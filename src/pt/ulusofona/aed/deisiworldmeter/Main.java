package pt.ulusofona.aed.deisiworldmeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class Main {
//aa
    static ArrayList<Cidade> infoCidades;
    static ArrayList<Paises> infoPaises;
    static ArrayList<Populacao> infoPopulacao;
    static int countPopulacaoInvalidos=0 ;
    static int countPaisesInvalidos= 0;
    static int  countCidadesInvalidos = 0;

    public static ArrayList<? extends Object> getObjects(TipoEntidade tipo) {

        ArrayList<Integer> intArrayList = new ArrayList<>();

        int[] ArrayCidade = {};
        int[] ArrayPais = {};
        int[] ArrayPopulacao = {};

        int linhasErradasCidades = 0;
        int linhasCertasCidades = 0;
        int linhasdoErroCidades = -1;


        if (tipo == TipoEntidade.PAIS){
           return infoPaises;
        }

        if (tipo == TipoEntidade.CIDADE){
            return infoCidades;
        }

        if (tipo == TipoEntidade.INPUT_INVALIDO){
            ArrayList<String> listaDeStrings = new ArrayList<>();

            // Adicionando strings ao ArrayList
            listaDeStrings.add("Primeira string");
            listaDeStrings.add("Segunda string");
            listaDeStrings.add("Terceira string");


        }




        return new ArrayList<>();
    }

    public static boolean parseFiles(File pasta){
        File filePaises = new File(pasta, "paises.csv");
        File filePopulacao = new File(pasta, "populacao.csv");
        File fileCidades = new File(pasta, "cidades.csv");

        infoPaises = new ArrayList<>();

        infoCidades = new ArrayList<>();

        infoPopulacao = new ArrayList<>();





        try (BufferedReader reader = new BufferedReader(new FileReader(filePaises))) {
            String linha;
            reader.readLine();
            int checkPaisesRepetidos = 0;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                for (int i = 0; i < infoPaises.size(); i++) {
                     if(infoPaises.get(i).id ==Integer.parseInt(dados[0].trim())){
                         checkPaisesRepetidos++;
                     };
                }
                if (dados.length == 4 && !dados[0].isEmpty() && !dados[1].isEmpty()&&!dados[2].isEmpty()&&!dados[3].isEmpty() && checkPaisesRepetidos==0) {

                    int id = Integer.parseInt(dados[0].trim());
                    String alfa2 = dados[1];
                    String alfa3 = dados[2];
                    String nome = dados[3];
                    Paises pais = new Paises(id, alfa2, alfa3, nome);
                    infoPaises.add(pais);
                }else{

                   countPaisesInvalidos++;
                }
                checkPaisesRepetidos=0;
            }
        }catch (IOException e) {
            return false;
        }


        // Lê o arquivo de população
        try (BufferedReader reader = new BufferedReader(new FileReader(filePopulacao))) {
            String linha;
            int checkIdentificador=0;
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                int tamanho = infoPaises.size();
                for (int i = 0; i < infoPaises.size(); i++) {
                    int id = Integer.parseInt(dados[0]);
                    if(Objects.equals(infoPaises.get(i).id, id)) {
                        checkIdentificador++;
                        break;
                    }
                }
                if (dados.length == 5 && dados[1].matches("\\d+")&&checkIdentificador >0) {
                    checkIdentificador = 0;
                    int id = Integer.parseInt(dados[0].trim());
                    int ano = Integer.parseInt(dados[1].trim());
                    long popMasculina = Long.parseLong(dados[2].trim());
                    long popFeminina = Long.parseLong(dados[3].trim());
                    Double densidade = Double.parseDouble(dados[4].trim());
                    Populacao populacao = new Populacao(id, ano, popMasculina, popFeminina, densidade);
                    infoPopulacao.add(populacao);

                }else{

                    countPopulacaoInvalidos++;
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
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                int checkPais = 0; // Reset checkPais for each new line

                for (int i = 0; i < infoPaises.size(); i++) {
                    if (Objects.equals(infoPaises.get(i).alfa2, dados[0])) {
                        checkPais++;
                        break;
                    }
                }

                if (dados.length == 6 && checkPais > 0 &&!dados[3].isEmpty()) {
                    String alfa2 = dados[0];
                    String cidade = dados[1];
                    int regiao = Integer.parseInt(dados[2].trim());
                   // Double populacao = !dados[3].isEmpty() ? Double.parseDouble(dados[3]) : null;
                    Double populacao = Double.parseDouble(dados[3]);
                    Double latitude = Double.parseDouble(dados[4]);
                    Double longitude = Double.parseDouble(dados[5]);
                    Cidade cidades = new Cidade(alfa2, cidade, regiao, populacao, latitude, longitude);
                    infoCidades.add(cidades);
                } else {
                    countCidadesInvalidos++;
                }
            }
        } catch (IOException e) {
            return false;
        }

        int tamanho = infoPopulacao.size();
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