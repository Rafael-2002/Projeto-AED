package pt.ulusofona.aed.deisiworldmeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.*;


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
    static int  primeiraLinha = 0;
    static ArrayList<Integer> linhasErradas = new ArrayList<>();

    public static ArrayList<? extends Object> getObjects(TipoEntidade tipo) {

        inputs.clear();


        if (tipo == TipoEntidade.PAIS){
            return infoPaises;
        }

        if (tipo == TipoEntidade.CIDADE){
            return infoCidades;
        }

        if (tipo == TipoEntidade.INPUT_INVALIDO){
            // Adicionando strings ao ArrayList
            inputs.add("paises.csv | " + infoPaises.size() + " | " + countPaisesInvalidos + " | " + linhaPais);
            inputs.add("cidades.csv | " + infoCidades.size() + " | " + countCidadesInvalidos + " | " + linhaCidades);
            inputs.add("populacao.csv | " + infoPopulacao.size() + " | " + countPopulacaoInvalidos + " | " + linhaPopulacao);

            return inputs;
        }
        return new ArrayList<>();
    }
    public static boolean parseFiles(File pasta){

        countCidadesInvalidos = 0;
        countPopulacaoInvalidos = 0;
        countPaisesInvalidos = 0;
        linhaCidades = -1;
        linhaPais = -1;
        linhaPopulacao = -1;
        primeiraLinha = 0;
        linhasErradas = new ArrayList<>();

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
                    }
                }
                if (dados.length == 4 && !dados[0].isEmpty() && !dados[1].isEmpty()&&!dados[2].isEmpty()&&!dados[3].isEmpty() && checkPaisesRepetidos==0) {

                    int id = Integer.parseInt(dados[0].trim());
                    String alfa2 = dados[1];
                    String alfa3 = dados[2];
                    String nome = dados[3];
                    Paises pais = new Paises(id, alfa2, alfa3, nome);
                    infoPaises.add(pais);
                    countLinha++;
                }else{
                    countLinha++;
                    countPaisesInvalidos++;
                    if(primeiraLinha == 0){
                        countLinha++;
                        linhaPais = countLinha;
                        primeiraLinha = 1;
                    }
                    linhasErradas.add(countLinha);
                }
                checkPaisesRepetidos=0;
            }
        }catch (IOException e) {
            return false;
        }

        primeiraLinha = 0;

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

                int checkCidadeRepetida = 0;




                if (dados.length == 6 && checkPais > 0 && !dados[3].isEmpty() && !dados[0].isEmpty() && !dados[2].isEmpty() && !dados[4].isEmpty() && !dados[5].isEmpty()) {
                    String alfa2 = dados[0];
                    String cidade = dados[1];
                    String regiao = dados[2];
                    // Double populacao = !dados[3].isEmpty() ? Double.parseDouble(dados[3]) : null;
                    Double populacao = Double.parseDouble(dados[3]);
                    Double latitude = Double.parseDouble(dados[4]);
                    Double longitude = Double.parseDouble(dados[5]);
                    Cidade cidades = new Cidade(alfa2, cidade, regiao, populacao, latitude, longitude);
                    infoCidades.add(cidades);
                    countLinha++;
                } else {
                    countLinha++;
                    countCidadesInvalidos++;
                    if(primeiraLinha == 0){
                        countLinha++;
                        linhaCidades = countLinha;
                        primeiraLinha = 1;
                    }

                    //minhaLista.add(countLinha);
                }
            }
        } catch (IOException e) {
            return false;
        }

        removePais();

        primeiraLinha = 0;

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
                if (dados.length == 5 && dados[1].matches("\\d+")&&checkIdentificador >0 && !dados[0].isEmpty() && !dados[1].isEmpty()&&!dados[2].isEmpty()&&!dados[3].isEmpty() && !dados[4].isEmpty()) {
                    checkIdentificador = 0;
                    int id = Integer.parseInt(dados[0].trim());
                    int ano = Integer.parseInt(dados[1].trim());
                    long popMasculina = Long.parseLong(dados[2].trim());
                    long popFeminina = Long.parseLong(dados[3].trim());
                    Double densidade = Double.parseDouble(dados[4].trim());
                    Populacao populacao = new Populacao(id, ano, popMasculina, popFeminina, densidade);
                    infoPopulacao.add(populacao);
                    countLinha++;

                }else{
                    countLinha++;
                    countPopulacaoInvalidos++;
                    if(primeiraLinha == 0){
                        countLinha++;
                        linhaPopulacao = countLinha;
                        primeiraLinha = 1;
                    }
                }
            }
        }catch (IOException e) {
            return false;
        }

        primeiraLinha = 0;

        return true;
    }
    public static double formulaGenderGap(long popMasculina, long popFeminina) {
        // Perform the calculation
        double result = ((double) (popMasculina - popFeminina) / (popMasculina + popFeminina)) * 100;

        // Round to two decimal places without rounding
        result = Math.round(result * 100.0) / 100.0;

        return result;
    }
    public static double formulaPopIcrease(long popTotalAtual, long popTotalAnterior) {
        Double test = (double) (popTotalAtual - popTotalAnterior);
        if (test <0){
            return 0.0;
        }
        double result = (test / (popTotalAtual)) * 100;

        result = Math.round(result * 100.0) / 100.0;
        return result;
    }

    public static void removePais(){

        HashSet<String> alfa2Cidades = new HashSet<>();
        for (Cidade cidade : infoCidades) {
            alfa2Cidades.add(cidade.alfa2);
        }


        for(int i = 0; i < infoPaises.size(); i++){
            if(!alfa2Cidades.contains(infoPaises.get(i).alfa2)){
                countPaisesInvalidos++;
                if(linhaPais > i + 2){
                    linhaPais = i + 2;
                }
                infoPaises.remove(i);
            }
        }
    }

    public static String getCountryNameById(int countryId) {
        for (Paises pais : infoPaises) {
            if (pais.id == countryId) {
                return pais.nome;
            }
        }
        return "";
    }
    public static Result execute(String comando) {
        String[] s = {"QUIT", "HELP", "COUNT_CITIES", "GET_CITIES_BY_COUNTRY", "SUM_POPULATIONS", "GET_HISTORY", "GET_TOP_POPULATION_INCREASE", "GET_MISSING_HISTORY", "GET_MOST_POPULOUS", "GET_TOP_CITIES_BY_COUNTRY", "GET_DUPLICATE_CITIES", "GET_COUNTRIES_GENDER_GAP", "GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES", "GET_CITIES_AT_DISTANCE", "GET_CITIES_AT_DISTANCE2","GET_COUNTRY_DETAILS", "INSERT_CITY", "REMOVE_COUNTRY"};
        boolean checkComandoValido = false;
        String stringResult = "";
        Result queries = new Result(false, null, stringResult);


        String[]  parts= comando.split("\\s+",2);
        String command = parts[0];

        if (!Objects.equals(command, "SUM_POPULATIONS") && !Objects.equals(command, "GET_CITIES_BY_COUNTRY") &&
                !Objects.equals(command, "GET_HISTORY") && !Objects.equals(command, "GET_TOP_CITIES_BY_COUNTRY")){
            parts = comando.split("\\s+");
        }

        if(Objects.equals(command, "GET_HISTORY")){
            parts = comando.split("\\s+", 4);
        }

        if(Objects.equals(command, "GET_CITIES_BY_COUNTRY") || Objects.equals(command, "GET_TOP_CITIES_BY_COUNTRY")){
            parts = comando.split("\\s+", 3);
        }

        for (String validCommand : s) {
            if (command.equals(validCommand)) {
                checkComandoValido = true;
                break;
            }
        }
        HashMap<Integer, String> IdPaises = new HashMap<>();
        for (int i = 0; i < infoPaises.size(); i++) {
            IdPaises.put(infoPaises.get(i).id, infoPaises.get(i).nome);
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
                                "GET_DUPLICATE_CITIES_DIFFERENT_COUNTRIES <min-population>\n" +
                                "GET_CITIES_AT_DISTANCE <distance> <country-name>\n" +
                                "GET_COUNTRY_DETAILS <country-name>\n" +
                                "INSERT_CITY <alfa2> <city-name> <region> <population>\n" +
                                "REMOVE_COUNTRY <country-name>\n" +
                                "HELP\n" +
                                "QUIT");
                stringResult = "";
                break;


            case "GET_COUNTRY_DETAILS":
                String paisNome = parts[1];
                int id = 0;
                String alfa2Pais2 = "";
                String alfa3Pais = "";
                int numCidadePais = 0;
                int popTotalF = 0;
                int popTotalM = 0;
                for(int i = 0; i < infoPaises.size(); i++){
                    if(infoPaises.get(i).nome.equals(paisNome)){
                        alfa2Pais2 = infoPaises.get(i).alfa2;
                        alfa3Pais = infoPaises.get(i).alfa3;
                        id = infoPaises.get(i).id;
                        break;
                    }
                }

                for(int i = 0; i < infoCidades.size(); i++){
                    if(alfa2Pais2.equals(infoCidades.get(i).alfa2)){
                        numCidadePais++;
                    }
                }

                for(int i = 0; i < infoPopulacao.size(); i++){
                    if(id == infoPopulacao.get(i).id){
                        if(infoPopulacao.get(i).ano < 2025) {
                            popTotalM += infoPopulacao.get(i).popMasculina;
                            popTotalF += infoPopulacao.get(i).popFeminina;
                        }
                    }
                }

                stringResult = "Pais: " + paisNome + "\nAlfa2: " + alfa2Pais2 + "\nAlfa3: " + alfa3Pais + "\nNumero de cidades: " + numCidadePais + "\nPopulação Masculina desde 1950: " + popTotalM + "\nPopulação Feminina desde 1950: " + popTotalF;

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
                    if (Objects.equals(infoPaises.get(i).nome, nomePais)) {
                        checkcount++;
                    }

                }
                if (checkcount > 0) {


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
                        stringResult += nomesPaises.get(j) + "\n";
                    }

                } else {
                    stringResult = "Pais invalido: " + nomePais;
                }
                break;


            case "SUM_POPULATIONS":
                String[] paisesParaSomar = parts[1].split(",");
                long populacaoTotal = 0;
                ArrayList<String> paisesInvalidosSP = new ArrayList<>();
                ArrayList<String> paisesSP = new ArrayList<>();
                for (int i = 0; i < infoPaises.size(); i++) {
                    paisesSP.add(infoPaises.get(i).nome); // Using add() instead of set()
                }
                boolean checkPaisesExistem = false;
                for (int i = 0; i < paisesParaSomar.length; i++) {
                    if (!paisesSP.contains(paisesParaSomar[i])) {
                        checkPaisesExistem = true;
                        paisesInvalidosSP.add(paisesParaSomar[i]);
                    }
                }
                for (String nomePaisS : paisesParaSomar) {
                    for (Paises pais : infoPaises) {
                        if (pais.nome.equalsIgnoreCase(nomePaisS.trim())) {
                            for (Populacao populacao : infoPopulacao) {
                                if (populacao.id == pais.id && populacao.ano == 2024) {
                                    populacaoTotal += populacao.popMasculina + populacao.popFeminina;
                                    break; // Saia do loop interno, pois jÃ¡ encontramos o paÃ­s correspondente
                                }
                            }
                            break; // Saia do loop externo, pois jÃ¡ encontramos o paÃ­s correspondente
                        }
                    }
                }
                if (populacaoTotal == 0 || checkPaisesExistem) {

                    stringResult = "Pais invalido: ";
                    for (int i = 0; i < paisesInvalidosSP.size(); i++) {
                        stringResult += paisesInvalidosSP.get(i);
                    }
                } else {
                    stringResult = String.valueOf(populacaoTotal);
                }
                break;

            case "GET_HISTORY":
                String anoInicioH = parts[1];
                String anoFimH = parts[2];
                String PaisH = parts[3];
                int idPaisH = 0;
                for (int paisIdH = 0; paisIdH < infoPaises.size(); paisIdH++) {
                    if (Objects.equals(infoPaises.get(paisIdH).nome, PaisH)) {
                        idPaisH = infoPaises.get(paisIdH).id;
                        break; // Achou o paÃ­s, pode sair do loop
                    }
                }
                for (int popH = 0; popH < infoPopulacao.size(); popH++) {
                    if (infoPopulacao.get(popH).id == idPaisH) {
                        if (infoPopulacao.get(popH).ano >= Integer.parseInt(anoInicioH) && infoPopulacao.get(popH).ano <= Integer.parseInt(anoFimH)) {
                            stringResult += infoPopulacao.get(popH).ano + ":" + (infoPopulacao.get(popH).popMasculina / 1000) + "k:" + (infoPopulacao.get(popH).popFeminina / 1000) + "k\n";
                        }
                    }
                }
                break;
            case "GET_MISSING_HISTORY":
                String anoInicioMH = parts[1];
                String anoFimMH = parts[2];
                ArrayList<Integer> anoInicioFim = new ArrayList<>();
                for (int i = Integer.parseInt(anoInicioMH); i <= Integer.parseInt(anoFimMH); i++) {
                    anoInicioFim.add(i);
                }

                Set<Integer> idPaisesMissing = new HashSet<>();
                HashMap<Integer, List<Integer>> idPaisesMH = new HashMap<>();

                for (Populacao populacao : infoPopulacao) {
                    if (!idPaisesMH.containsKey(populacao.id)) {
                        idPaisesMH.put(populacao.id, new ArrayList<>());
                    }
                    idPaisesMH.get(populacao.id).add(populacao.ano);
                }


                for (Map.Entry<Integer, List<Integer>> entry : idPaisesMH.entrySet()) {
                    int idPais = entry.getKey();
                    List<Integer> anosPais = entry.getValue();
                    for (int ano : anoInicioFim) {
                        if (!anosPais.contains(ano)) {
                            idPaisesMissing.add(idPais);
                            break;
                        }
                    }
                }

                ArrayList<String> alfa2MH = new ArrayList<>();
                ArrayList<String> paisesMH = new ArrayList<>();
                for (int idPaisMH : idPaisesMissing) {
                    for (Paises pais : infoPaises) {
                        if (pais.id == idPaisMH && !alfa2MH.contains(pais.alfa2)) {
                            alfa2MH.add(pais.alfa2);
                            paisesMH.add(pais.nome);
                            break; // Exit the loop once we find the corresponding country
                        }
                    }
                }

                if (idPaisesMissing.isEmpty()) {
                    stringResult = "Sem resultados";
                } else {
                    for (int i = 0; i < paisesMH.size(); i++) {
                        stringResult += alfa2MH.get(i) + ":" + paisesMH.get(i) + "\n";
                    }
                }
                break;

            case "INSERT_CITY":
                String alfa2IS = parts[1];
                String nomeCidadeIS = parts[2];
                String regiaoIS = parts[3];
                String populacaoIS = parts[4];

                boolean checkPaisExisteIS = false;


                for (Paises infoPaisIC : infoPaises) {
                    if (infoPaisIC.alfa2.equals(alfa2IS)) {
                        checkPaisExisteIS = true;
                        break;
                    }
                }

                if (!checkPaisExisteIS) {
                    stringResult = "Pais invalido";
                } else {
                    // Country exists, so create and add the city
                    Cidade cidadeIS = new Cidade(alfa2IS, nomeCidadeIS, regiaoIS, Double.parseDouble(populacaoIS));
                    infoCidades.add(cidadeIS);
                    stringResult = "Inserido com sucesso";
                }
                break;
            case "REMOVE_COUNTRY":
                String nomePaisRC = parts[1];
                boolean checkPaisExisteRC = false;
                for (int i = 0; i < infoPaises.size(); i++) {
                    if (infoPaises.get(i).nome.equals(nomePaisRC)) {
                        checkPaisExisteRC = true;
                        infoPaises.remove(i);
                        i--; // Adjust the index after removing the element
                    }
                }
                if (!checkPaisExisteRC) {
                    stringResult = "Pais invalido";
                } else {
                    stringResult = "Removido com sucesso";
                }
                break;


            case "GET_COUNTRIES_GENDER_GAP":
                String genderPI = parts[1];


                HashMap<String, Double> genderGapPI = new HashMap<>();
                for (int i = 0; i < infoPopulacao.size(); i++) {
                    if (infoPopulacao.get(i).ano == 2024) {

                        if (formulaGenderGap(infoPopulacao.get(i).popMasculina, infoPopulacao.get(i).popFeminina) <= Integer.parseInt(genderPI)) {
                            genderGapPI.put(IdPaises.get(infoPopulacao.get(i).id), formulaGenderGap(infoPopulacao.get(i).popMasculina, infoPopulacao.get(i).popFeminina));
                        }
                    }
                }

                for (String key : genderGapPI.keySet()) {
                    stringResult += key + ":" + genderGapPI.get(key);
                }
                break;


            case "GET_TOP_POPULATION_INCREASE":
                String anoInicioPI = parts[1];
                String anoFimPI = parts[2];
                ArrayList<Integer> anosPI = new ArrayList<>();
                for (int i = Integer.parseInt(anoInicioPI); i <= Integer.parseInt(anoFimPI); i++) {
                    anosPI.add(i);
                }

                TreeMap<Double, String[]> topIncreases = new TreeMap<>(Collections.reverseOrder());

                for (int i = 0; i < anosPI.size() - 1; i++) {
                    for (int j = 0; j < anosPI.size() ; j++) {
                        int year1 = anosPI.get(i);
                        int year2 = anosPI.get(j);
                        for (Populacao pop1 : infoPopulacao) {
                            if (pop1.ano == year1) {
                                for (Populacao pop2 : infoPopulacao) {
                                    if (pop2.ano == year2 && pop1.id == pop2.id) {

                                        double increase = formulaPopIcrease(pop2.popMasculina + pop2.popFeminina, pop1.popMasculina + pop1.popFeminina);
                                        if (increase > 0) {
                                            String[] data = {String.valueOf(year1), String.valueOf(year2), getCountryNameById(pop1.id)};
                                            topIncreases.put(increase, data);
                                            if (topIncreases.size() > 5) {
                                                topIncreases.pollLastEntry();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Format the output
                StringBuilder result = new StringBuilder();

                DecimalFormat df = new DecimalFormat("0.00");
                for (Map.Entry<Double, String[]> entry : topIncreases.entrySet()) {
                    double increase = entry.getKey();
                    String[] data = entry.getValue();
                    result.append(data[2]).append(":").append(data[0]).append("-").append(data[1]).append(":").append(df.format(increase)).append("%").append("\n");

                }
                stringResult = result.toString();
                break;



            case "GET_MOST_POPULOUS":
                int numCidades = Integer.parseInt(parts[1]);
                ArrayList<String> nomesCidades = new ArrayList<>();
                ArrayList<Cidade> infoCidadesOrdenado = ordenarCidadesQuickSort(infoCidades.clone(), 0, infoCidades.size());


                for(int i = 0; i <infoCidadesOrdenado.size();i++){
                    if(numCidades <= 0){
                        break;
                    }
                    if(!nomesCidades.contains(infoCidadesOrdenado.get(i).alfa2)){
                        nomesCidades.add(infoCidadesOrdenado.get(i).alfa2);
                        String pais = "";
                        for(int y = 0; y < infoPaises.size(); y++){
                            if(infoCidadesOrdenado.get(i).alfa2.equals(infoPaises.get(y).alfa2)){
                                pais = infoPaises.get(y).nome;
                                break;
                            }
                        }

                        String scientificNotation = String.valueOf(infoCidadesOrdenado.get(i).populacao);
                        BigDecimal bigDecimal = new BigDecimal(scientificNotation);
                        String decimalForm = bigDecimal.toPlainString();
                        String withoutDecimalZero = decimalForm.replace(".0", "");

                        numCidades--;
                        stringResult += pais + ":" + infoCidadesOrdenado.get(i).cidade + ":" + withoutDecimalZero + "\n";
                    }
                }

                break;


            case "GET_TOP_CITIES_BY_COUNTRY":
                String pais = parts[2];
                ArrayList<Cidade> cidadesPais = new ArrayList<>();
                String alfa2Pais = "";
                int numeroPaises = Integer.parseInt(parts[1]);
                int x2 = 1;




                for(int i = 0; i < infoPaises.size(); i++){
                    if(parts[2].equals(infoPaises.get(i).nome)){
                        alfa2Pais = infoPaises.get(i).alfa2;
                        break;
                    }
                }

                for(int i = 0; i < infoCidades.size();i++){
                    if(infoCidades.get(i).alfa2.equals(alfa2Pais)){
                        cidadesPais.add(infoCidades.get(i));
                    }
                }

                ArrayList<Cidade> infoCidadesOrdenado2 = ordenarCidadesQuickSort(cidadesPais.clone(), 0, cidadesPais.size());

                for(int i = 0; i < infoCidadesOrdenado2.size(); i++){
                    while (x2 < infoCidadesOrdenado2.size()){

                        int popx2 = (int) (infoCidadesOrdenado2.get(x2).populacao / 1000);
                        int popi = (int) (infoCidadesOrdenado2.get(i).populacao / 1000);

                        if(popx2 == popi){
                            int comparison = infoCidadesOrdenado2.get(i).cidade.compareTo(infoCidadesOrdenado2.get(x2).cidade);

                            if (comparison > 0) {
                                Collections.swap(infoCidadesOrdenado2, x2, i);
                            }
                        }
                        x2++;
                    }
                    x2 = i + 1;
                }


                if(numeroPaises == -1){
                    for (int i = 0; i < infoCidadesOrdenado2.size(); i++) {
                        if(infoCidadesOrdenado2.get(i).populacao >= 10000){
                            stringResult += infoCidadesOrdenado2.get(i).cidade + ":" + infoCidadesOrdenado2.get(i).getFormattedPopulacao() + "\n";
                        }
                    }
                }else{
                    for (int i = 0; i < numeroPaises; i++) {
                        stringResult += infoCidadesOrdenado2.get(i).cidade + ":" + infoCidadesOrdenado2.get(i).getFormattedPopulacao() + "\n";
                    }
                }
                break;


            case "GET_DUPLICATE_CITIES":

                ArrayList<Cidade> cidadesRepetidas = new ArrayList<>();
                ArrayList<String> cidadesOriginais = new ArrayList<>();
                int numeroPop = Integer.parseInt(parts[1]);
                String nomeP = "";

                for(int i = 0; i < infoCidades.size(); i++){
                    if(cidadesOriginais.contains(infoCidades.get(i).cidade)){
                        cidadesRepetidas.add(infoCidades.get(i));
                    }else{
                        cidadesOriginais.add(infoCidades.get(i).cidade);
                    }
                }

                for(int i = 0; i < cidadesRepetidas.size();i++){
                    if(cidadesRepetidas.get(i).populacao >= numeroPop){

                        for(int x = 0; x < infoPaises.size(); x++){
                            if(cidadesRepetidas.get(i).alfa2.equals(infoPaises.get(x).alfa2)){
                                nomeP = infoPaises.get(x).nome;
                                break;
                            }
                        }
                        stringResult += cidadesRepetidas.get(i).cidade + " (" + nomeP + "," + cidadesRepetidas.get(i).regiao + ")\n";
                    }
                }


                break;

            case "GET_CITIES_AT_DISTANCE":

                String alfa2P = "";
                double distancia = 0;
                int dist = Integer.parseInt(parts[1]);
                String cidadeI;
                String cidadeY;

                for(int i = 0; i < infoPaises.size(); i++){
                    if(parts[2].equals(infoPaises.get(i).nome)){
                        alfa2P = infoPaises.get(i).alfa2;
                        break;
                    }
                }
                int y = 1;

                for(int i = 0; i < infoCidades.size(); i++){
                    while (y < infoCidades.size()){
                        if(infoCidades.get(i).alfa2.equals(alfa2P) && infoCidades.get(y).alfa2.equals(alfa2P)){
                            distancia = distance(infoCidades.get(i).latitude,infoCidades.get(i).longitude, infoCidades.get(y).latitude, infoCidades.get(y).longitude);
                            cidadeI = String.valueOf(infoCidades.get(i));
                            cidadeY = String.valueOf(infoCidades.get(y));

                            if(distancia >= dist - 1 && distancia <= dist + 1){
                                int comparison = cidadeI.compareTo(cidadeY);

                                if (comparison < 0) {

                                    // str1 vem antes de str2
                                    stringResult += infoCidades.get(i).cidade + "->" + infoCidades.get(y).cidade + "\n";
                                } else if (comparison > 0) {
                                    // str2 vem antes de str1
                                    stringResult += infoCidades.get(y).cidade + "->" + infoCidades.get(i).cidade + "\n";
                                } else {
                                    stringResult += infoCidades.get(i).cidade + "->" + infoCidades.get(y).cidade + "\n";
                                }
                            }
                        }
                        y++;
                    }
                    y = i + 1;
                }


                break;
        }
        if (checkComandoValido) {
            queries = new Result(true, null, stringResult);
        } else {

            queries = new Result(false, null, "Command not recognized");
        }

        return queries;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to DEISI World Meter");

        long start = System.currentTimeMillis();
        boolean parselk = parseFiles(new File("ficheirosGrandes"));
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



    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6371 * c; // Earth radius
    }

    public static ArrayList<Cidade> ordenarCidadesQuickSort(Object cidades, int left, int right) {
        if (left < right) {
            int posicaoPivot = partition((ArrayList<Cidade>) cidades, left, right - 1);
            cidades = ordenarCidadesQuickSort(cidades, left, posicaoPivot);
            cidades = ordenarCidadesQuickSort(cidades, posicaoPivot + 1, right);
        }
        return (ArrayList<Cidade>) cidades;
    }

    public static int partition(ArrayList<Cidade> cidades, int left, int right) {
        Cidade pivot = cidades.get(right);

        int leftIdx = left;
        int rightIdx = right - 1;

        while (leftIdx <= rightIdx) {
            if (cidades.get(leftIdx).getPopulacao() < pivot.getPopulacao() && cidades.get(rightIdx).getPopulacao() > pivot.getPopulacao()) {
                Cidade temp = cidades.get(leftIdx);
                cidades.set(leftIdx, cidades.get(rightIdx));
                cidades.set(rightIdx, temp);
                leftIdx++;
                rightIdx--;
            } else {
                if (cidades.get(leftIdx).getPopulacao() >= pivot.getPopulacao()) {
                    leftIdx++;
                }
                if (cidades.get(rightIdx).getPopulacao() <= pivot.getPopulacao()) {
                    rightIdx--;
                }
            }
        }

        cidades.set(right, cidades.get(leftIdx));
        cidades.set(leftIdx, pivot);

        return leftIdx;
    }
}