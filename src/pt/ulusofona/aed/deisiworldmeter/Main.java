package pt.ulusofona.aed.deisiworldmeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class Main {
//aa
    static ArrayList<Cidade> infoCidades;
    static ArrayList<Paises> infoPaises;
    static ArrayList<Populacao> infoPopulacao;
    static ArrayList<String> inputs = new ArrayList<>();
    static int countPopulacaoInvalidos=0;
    static int countPaisesInvalidos= 0;
    static int  countCidadesInvalidos = 0;
    static int  linhaCidades = -1;
    static int  linhaPais = -1;
    static int  linhaPopulacao = -1;


    public static ArrayList<? extends Object> getObjects(TipoEntidade tipo) {

        ArrayList<Integer> intArrayList = new ArrayList<>();

        int[] ArrayCidade = {};
        int[] ArrayPais = {};
        int[] ArrayPopulacao = {};


        if (tipo == TipoEntidade.PAIS){
           return infoPaises;
        }

        if (tipo == TipoEntidade.CIDADE){
            return infoCidades;
        }

        if (tipo == TipoEntidade.INPUT_INVALIDO){
            // Adicionando strings ao ArrayList
            inputs.add("nome | linhas OK | linhas NOK | primeira linha NOK");
            inputs.add("paises.csv | " + infoPaises.size() + "| " + countPaisesInvalidos + "| " + linhaPais);
            inputs.add("cidades.csv | " + infoCidades.size() + "| " + countCidadesInvalidos + "| " + linhaCidades);
            inputs.add("populacao.csv | " + infoPopulacao.size() + "| " + countPopulacaoInvalidos + "| " + linhaPopulacao);

            return inputs;
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
            int countLinha = 0;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                for (int i = 0; i < infoPaises.size(); i++) {
                     if(infoPaises.get(i).id ==Integer.parseInt(dados[0].trim())){
                         checkPaisesRepetidos++;
                     };
                }
                boolean checkpaistemcidades = false;
                int i = 0;
                while(!checkpaistemcidades && i != infoCidades.size()){
                    if (Objects.equals(infoCidades.get(i).alfa2, dados[1])){
                        checkpaistemcidades = true;

                    }
                    i++;
                }
                if (dados.length == 4 && !dados[0].isEmpty() && !dados[1].isEmpty()&&!dados[2].isEmpty()&&!dados[3].isEmpty() && checkPaisesRepetidos==0&& !checkpaistemcidades) {

                    int id = Integer.parseInt(dados[0].trim());
                    String alfa2 = dados[1];
                    String alfa3 = dados[2];
                    String nome = dados[3];
                    Paises pais = new Paises(id, alfa2, alfa3, nome);
                    infoPaises.add(pais);
                }else{

                   countPaisesInvalidos++;
                   linhaPais = countLinha;
                }
                checkPaisesRepetidos=0;

                countLinha++;
            }
        }catch (IOException e) {
            return false;
        }


        // Lê o arquivo de população
        try (BufferedReader reader = new BufferedReader(new FileReader(filePopulacao))) {
            String linha;
            int checkIdentificador=0;
            int countLinha = 0;
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
                    linhaPopulacao = countLinha;
                }

                countLinha++;
            }
        }catch (IOException e) {
            return false;
        }


        // Lê o arquivo de cidades
        try (BufferedReader reader = new BufferedReader(new FileReader(fileCidades))) {
            String linha;
            int countLinha = 0;
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

                boolean val = true;

                try {
                    if (dados.length >= 3){
                    Double numero = Double.parseDouble(dados[2]);
                    }
                } catch (NumberFormatException e) {
                    val = false;
                }

                if (dados.length == 6 && checkPais > 0 &&!dados[3].isEmpty() && val) {
                    String alfa2 = dados[0];
                    String cidade = dados[1];
                    String regiao = dados[2];
                    // Double populacao = !dados[3].isEmpty() ? Double.parseDouble(dados[3]) : null;
                    Double populacao = Double.parseDouble(dados[3]);
                    Double latitude = Double.parseDouble(dados[4]);
                    Double longitude = Double.parseDouble(dados[5]);
                    Cidade cidades = new Cidade(alfa2, cidade, regiao, populacao, latitude, longitude);
                    infoCidades.add(cidades);
                } else {
                    countCidadesInvalidos++;
                    linhaCidades = countLinha;
                }

                countLinha++;
            }
        } catch (IOException e) {
            return false;
        }

        int tamanho = infoPopulacao.size();
        return true;
    }

    public static Result execute(String comando) {
        String[] s = {"QUIT","HELP","COUNT_CITIES", "GET_CITIES_BY_COUNTRY", "SUM_POPULATIONS", "GET_HISTORY", "GET_MISSING_HISTORY", "GET_MOST_POPULOUS", "GET_TOP_CITIES_BY_COUNTRY", "GET_DUPLICATE_CITIES", "GET_COUNTRIES_GENDER_GAP", "GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES", "GET_CITIES_AT_DISTANCE", "GET_CITIES_AT_DISTANCE2", "INSERT_CITY", "REMOVE_COUNTRY"};
        boolean checkcomandovalido = false;
        String stringResult = "";
        Result queries = new Result(false, null, stringResult.toString());


        String[] parts = comando.split("\\s+");
        String command = parts[0];


        for (String validCommand : s) {
            if (command.equals(validCommand)) {
                checkcomandovalido = true;
                break;
            }
        }
       switch (parts[0]) {
           case "HELP":
               System.out.println(
                       "Commands available:\n" +
                               "COUNT_CITIES <min_population>\n" +
                               "GET_CITIES_BY_COUNTRY <num-results> <country-name>\n" +
                               "SUM_POPULATIONS <countries-List>\n" +
                               "GET_HISTORY <year-start> <year-end> <country_name>\n" +
                               "GET_MISSING_HISTORY <year-start> <year-end>\n" +
                               "GET_MOST_POPULOUS <num-results>\n" +
                               "GET_TOP_CITIES_BY_COUNTRY <num-results> <country-name> \n" +
                               "GET_DUPLICATE_CITIES <min_population> \n" +
                               "GET_COUNTRIES_GENDER_GAP <min-gender-gap>\n" +
                               "GET_TOP_POPULATION_INCREASE <year-start> <year_end>\n" +
                               "GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES <min-population> GET_CITIES_AT_DISTANCE <distance> <country-name>\n" +
                               "INSERT_CITY <alfa2> <city-name> <region> <population> REMOVE_COUNTRY <country-name>\n" +
                               "HELP\n" +
                               "QUIT");
               stringResult = "";
               break;
           case "COUNT_CITIES":
               int countCities = 0;
               for (int cities = 0; cities < infoCidades.size(); cities++) {
                   if (infoCidades.get(cities).populacao >= Double.parseDouble(parts[1])) {
                       countCities++;

                   }
               }
               stringResult = String.valueOf(countCities);
               break;

           case "GET_CITIES_BY_COUNTRY":
               int numResultados = Integer.parseInt(parts[1]);
               String nomePais = parts[2];
               String alfa2 = "";
               int count = 0;
               ArrayList<String> nomesPaises = new ArrayList<>();
               int checkcount = 0;
               for (int i = 0; i < infoPaises.size(); i++) {
                   if(Objects.equals(infoPaises.get(i).nome, nomePais)){
                   checkcount++;
                   }

               }
               if (checkcount>0) {


                   for (int j = 0; j < infoPaises.size(); j++) {
                       if (Objects.equals(infoPaises.get(j).nome, nomePais)) {
                           alfa2 = String.valueOf(infoPaises.get(j).alfa2);
                           break;
                       }
                   }

                   for (int j = 0; j < infoCidades.size(); j++) {
                       if (Objects.equals(infoCidades.get(j).alfa2, alfa2)) {
                           count++;
                           nomesPaises.add(infoCidades.get(j).cidade);
                       }
                       if (String.valueOf(count).equals(parts[1])) {

                           break;
                       }
                   }
                   for (int j = 0; j < nomesPaises.size(); j++) {
                       stringResult+=nomesPaises.get(j)+"\n";
                   }

               }else{
                   stringResult = "Pais invalido: "+nomePais;
               }

           case "SUM_POPULATIONS":
               String[] partsPaises = parts[1].split(",");
               long popTotal=0;
               ArrayList<Integer> alfa2array = new ArrayList<>();
               for (int i = 0; i < partsPaises.length; i++) {
               for (int paises = 0; paises < infoPaises.size(); paises++) {


                   if (Objects.equals(infoPaises.get(paises).nome, partsPaises[i])) {
                       alfa2array.add((infoPaises.get(paises).id));
                       break;
                   }
                   }
               }
               for (int i = 0; i < infoPopulacao.size(); i++) {
                   for (int j = 0; j < alfa2array.size(); j++) {
                       if(infoPopulacao.get(i).id == alfa2array.get(j)){
                          popTotal+= infoPopulacao.get(i).popFeminina +infoPopulacao.get(i).popMasculina;
                       }
                   }
               }
               stringResult = String.valueOf(popTotal);
        }


        if (checkcomandovalido) {
            queries = new Result(true, null, stringResult);
        }else{

            queries = new Result(false, null, "Command not recognized");
        }

        return queries;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to DEISI World Meter");

        long start = System.currentTimeMillis();
        boolean parselk = parseFiles(new File("ficheirosPequenos"));
        if (!parselk) {
            System.out.println("Error loading Files");
        }
      long end = System.currentTimeMillis();
        System.out.println("Loaded files in " + (end - start) + " ms");

        Result queries = execute("HELP");
        System.out.println(queries.result);

        Scanner in = new Scanner(System.in);
        String line;
        do{
            System.out.print("> ");
            line = in.nextLine();
            if(line != null && !line.equals ("QUIT")){
                start = System.currentTimeMillis();
                queries = execute(line);
                end = System.currentTimeMillis();
                if(!queries.success){
                    System.out.println("Error: "+ queries.error);

                }else{
                    System.out.println(queries.result);
                    System.out.println("(took "+ (end - start) +" ns)");
                }

            }
        }while(line!= null && !line.equals("QUIT"));


        }
}