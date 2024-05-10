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
        int resultado_esperado = 40603;
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
    @Test
    public  void Test_Obtem_Cidades(){
        String resultado_esperado = "andorra la vella | AD | 07 | 20430 | (42.5,1.5166667)";
        parseFiles(new File("."));
        String resultado_Atual = Main.infoCidades.toString();
        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");
  }

    @Test
    public  void Test_Pais_Tem_Cidades(){
        String resultado_esperado = "3";
        parseFiles(new File("ficheirosPequenos"));
        String resultado_Atual = String.valueOf(Main.getObjects(TipoEntidade.PAIS).size());
        assertEquals(resultado_esperado,resultado_Atual,"espetaculo");
    }

}
