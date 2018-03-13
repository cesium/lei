/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos;

/**
 *
 * @author Bruno
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.imageio.stream.FileImageInputStream;

import org.jgrapht.*;
import org.json.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.alg.cycle.*;
public class Grafos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JSONException, FileNotFoundException {
           
           /* Ler e guardar ficheiro JSON num objeto JSON */
           Scanner s= new Scanner(new FileReader("../../../estruturaDados.txt"));
           StringBuilder sb = new StringBuilder();
           
           while(s.hasNext()){
            sb.append(s.next());
           }
           s.close();
           JSONObject obj = new JSONObject(sb.toString());
           /* Ler e guardar ficheiro JSON num objeto JSON */
           
           /* Iterar Sobre o Objecto JSON */
           
           JSONArray keys = obj.names();
           for (int i = 0; i < keys.length (); ++i) {
               ArrayList<String> turnos = new ArrayList<>();
               ArrayList<Pretencao> arestas = new ArrayList<>();
               String uc = keys.getString (i); // tenho a key//
                JSONArray alunos = obj.getJSONArray(uc);
                for(int j=0;j<alunos.length();j++){ //iterar sobre as pretencoes dos alunos do turno // 
                   JSONObject aluno = alunos.getJSONObject(j); 
                   String turnoO=aluno.get("origem").toString();
                   String turnoD=aluno.get("destino").toString();
                   Number data=(Number)aluno.get("data");
                   Pretencao p= new Pretencao(data, turnoO,turnoD);
                   
                   if(!turnos.contains(turnoO)){
                       turnos.add(turnoO);
                   }
                   if(!turnos.contains(turnoD)){
                       turnos.add(turnoD);
                   }
                 
                   if(!arestas.contains(p)){
                       arestas.add(p);
                   }
                   
                }
               
                DefaultDirectedWeightedGraph<String,LPretencoes> graph;
                graph =new DefaultDirectedWeightedGraph<String, LPretencoes>(LPretencoes.class);

                for(String turno : turnos){
                 graph.addVertex(turno);
                }
               for(Pretencao aresta: arestas){
                     LPretencoes Lpreten=graph.getEdge(aresta.getOrigem(),aresta.getDestino());
                    if(Lpreten!=null){
                        graph.getEdge(aresta.getOrigem(),aresta.getDestino()).pretencoes.add(aresta);
                    }
                    else{
                        LPretencoes novaLp= new LPretencoes();
                        novaLp.pretencoes.add(aresta);
                        graph.addEdge(aresta.getOrigem(),aresta.getDestino(),novaLp);
                    }     
               }
               
             
             HawickJamesSimpleCycles hjsc; 
               hjsc = new HawickJamesSimpleCycles();
             hjsc.setGraph(graph);
             List l1= hjsc.findSimpleCycles();
                System.out.println("/////////////HawickJamesSimpleCycles///////////////////////");
                System.out.println(l1);
                System.out.println("////////////////////////////////////");
             
             TarjanSimpleCycles tsc;
             tsc=new TarjanSimpleCycles(); 
             tsc.setGraph(graph);
             List l2= tsc.findSimpleCycles();
                System.out.println("//////////////////TarjanSimpleCycles//////////////////");
                System.out.println(l2);
                System.out.println("////////////////////////////////////");
                
            SzwarcfiterLauerSimpleCycles slsc;
            slsc= new SzwarcfiterLauerSimpleCycles();
            slsc.setGraph(graph);
            List l3=slsc.findSimpleCycles();
            System.out.println("///////////////////SzwarcfiterLauerSimpleCycles/////////////////");
            System.out.println(l3);
             System.out.println("////////////////////////////////////");
                
             System.out.println("\n\n\n\n\n");
                
                /**
                Teste para ver se o grafo está bem gerado. 
               int w=0;
               for(LPretencoes p: graph.getAllEdges("TP1","TP2")){
                  System.out.println(p.pretencoes.get(w).origem);
                  System.out.println(p.pretencoes.get(w).destino);
                  System.out.println(p.pretencoes.get(w).data);
                    }
              **/
          
            }
    }
}
