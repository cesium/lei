/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import swap.swap_solver_v1.SwapSolver;

public class ExchangeResolverTest {
    String fileToString(File f){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
   
    @Test
    public void test1() {
        String testsDir = "./src/test/test_files/";
        File inputsFolder = new File(testsDir + "inputs/");
        File[] listOfInputs = inputsFolder.listFiles();
        for(File inputFile : listOfInputs){
            if (inputFile.isFile() && inputFile.getName().endsWith(".json")) {
                String fileName = inputFile.getName().split("\\.")[0];
                File resultFile = new File(testsDir + "expected_results/"+fileName+"_res.json");
                if(!resultFile.exists()){
                    continue;
                }
                    
                String line = null;
                String jsonInputString = fileToString(inputFile);
                String jsonResultString = fileToString(resultFile);
                
                JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
                JSONObject expectedResult = new JSONObject(jsonResultString);
                assertTrue(output.similar(expectedResult));
            }
        }

    }
}
