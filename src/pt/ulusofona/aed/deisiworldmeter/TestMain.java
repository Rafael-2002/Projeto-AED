package pt.ulusofona.aed.deisiworldmeter;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pt.ulusofona.aed.deisiworldmeter.Main.parseFiles;
public class TestMain {

    @Test
    public  void Test_Nr_Paises(){
        int resultado_esperado = 10;
        parseFiles(new File("."));
        int resultado_Atual = Main.infoPaises.size();

        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");

    }
    @Test
    public  void Test_Nr_Cidades(){
        int resultado_esperado = 0;
        parseFiles(new File("."));
        int resultado_Atual = Main.infoCidades.size();

        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");

    }
    @Test
    public  void Test_ToString_id_maior_Q_700(){

        String resultado_esperado = "Argentina | 701 | AR | ARG | 2";
        parseFiles(new File("."));
        String resultado_Atual = Main.infoPaises.toString();

        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");

  }




}
