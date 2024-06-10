package pt.ulusofona.aed.deisiworldmeter;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static pt.ulusofona.aed.deisiworldmeter.Main.parseFiles;
public class TestMain {
    @Test
    public  void GET_CITIES_BY_COUNTRY(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_CITIES_BY_COUNTRY 5 Portugal");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "abrantes",
                "abraveses",
                "adaufe",
                "a dos cunhados",
                "aguada de cima"
        }, resultParts);
    }

    @Test
    public  void SUM_POPULATIONS(){
        String resultado_esperado = "57696721";
        parseFiles(new File("test-files"));
        Result resultado =  Main.execute("SUM_POPULATIONS Portugal,Espanha");
        String resultado_Atual =resultado.result;
        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");
    }

    @Test
    public  void GET_HISTORY(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_HISTORY 2013 2024 Portugal");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                        "2013:4979k:5484k",
                        "2014:4943k:5464k",
                        "2015:4915k:5449k",
                        "2016:4895k:5437k",
                        "2017:4878k:5429k",
                        "2018:4862k:5427k",
                        "2019:4857k:5432k",
                        "2020:4859k:5438k",
                        "2021:4854k:5435k",
                        "2022:4846k:5424k",
                        "2023:4836k:5410k",
                        "2024:4827k:5396k"
        }, resultParts);
    }

    @Test
    public  void GET_MISSING_HISTORY(){
        String resultado_esperado = "Sem resultados";
        parseFiles(new File("test-files"));
        Result resultado =  Main.execute("GET_MISSING_HISTORY 2000 2024");
        String resultado_Atual =resultado.result;
        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");
    }

    @Test
    public  void GET_MOST_POPULOUS(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_MOST_POPULOUS 2");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "Espanha:malaga:557875",
                "Portugal:amadora:178856"
        }, resultParts);
    }

    @Test
    public  void GET_TOP_CITIES_BY_COUNTRY(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_TOP_CITIES_BY_COUNTRY 5 Portugal");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "amadora:178K",
                "amora:52K",
                "almada:34K",
                "alcabideche:33K",
                "aguas santas:27K"
        }, resultParts);
    }

    @Test
    public  void GET_DUPLICATE_CITIES(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_DUPLICATE_CITIES 1000");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "clinton (Estados Unidos,MO)",
        }, resultParts);
    }

    @Test
    public  void GET_COUNTRIES_GENDER_GAP(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_COUNTRIES_GENDER_GAP 2");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "Espanha:-1.96Estados Unidos:-1.02Portugal:-5.57",
        }, resultParts);
    }

    @Test
    public  void GET_TOP_POPULATION_INCREASE(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_TOP_POPULATION_INCREASE 2022 2024");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "Estados Unidos:2022-2024:1,03%",
                "Estados Unidos:2023-2024:0,53%",
                "Estados Unidos:2022-2023:0,50%",
                "Portugal:2023-2022:0,23%",
                "Espanha:2023-2022:0,08%"
        }, resultParts);
    }

    @Test
    public  void GET_CITIES_AT_DISTANCE(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_CITIES_AT_DISTANCE 20 Portugal");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "albufeira->almancil",
                "alcanede->alcobaca",
                "alcanede->aljubarrota",
                "alcochete->alhandra",
                "alcochete->almada",
                "alcochete->amora",
                "aldeia de paio pires->amadora",
                "alhos vedros->apelacao",
                "amora->apelacao"
        }, resultParts);
    }

    @Test
    public void testCreativeCommand(){
        assertTrue(Main.parseFiles(new File("test-files")));
        Result result = Main.execute("GET_COUNTRY_DETAILS Portugal");
        assertNotNull(result);
        assertTrue(result.success);
        String[] resultParts = result.result.split("\n");
        assertArrayEquals(new String[] {
                "Pais: Portugal",
                "Alfa2: pt",
                "Alfa3: prt",
                "Numero de cidades: 43",
                "População Masculina desde 1950: 348069837",
                "População Feminina desde 1950: 379090865"
        }, resultParts);
    }


}
