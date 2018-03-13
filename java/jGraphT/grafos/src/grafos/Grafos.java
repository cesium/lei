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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
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
            System.out.println(uc);
            JSONArray alunos = obj.getJSONArray(uc);
                for(int j=0;j<alunos.length();j++){ //iterar sobre as pretencoes dos alunos do turno // 
                   JSONObject aluno = alunos.getJSONObject(j); 
                   String turnoO=aluno.get("origem").toString();
                   String turnoD=aluno.get("destino").toString();
                   String mecanografico=aluno.get("aluno").toString();
                   Long data=(Long) aluno.get("data");
                   Pretencao p= new Pretencao(data, turnoO,turnoD,mecanografico);
                   
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
            TarjanSimpleCycles tsc;
            tsc=new TarjanSimpleCycles(); 
            tsc.setGraph(graph);
            List<List<String> > listaCiclos= tsc.findSimpleCycles();
            //System.out.println("////////////////////////////////////");
            //System.out.println(listaCiclos.toString());
            //System.out.println("////////////////////////////////////");
            if(listaCiclos.size()>0){
            //Existem ciclos no grafo
                int longest=listaCiclos.stream().mapToInt(List::size).max().orElse(-1);
                List<List<String>> maiorCiclos= listaCiclos.stream().filter(x->x.size()==longest).collect(toList());
                System.out.println("Maior ciclo:"+maiorCiclos.toString());
                List<String> ciclo_A_resolver=null;
                
                if(maiorCiclos.size()>1){
                 //Há Empate//   
                   Long minData=Long.MAX_VALUE;
                  for(List<String> ciclo: maiorCiclos){
                      ciclo.add(ciclo.get(0));
                     Long minCiclo=Long.MAX_VALUE;
                     for(int k=0;k<ciclo.size()-1;++k){
                      LPretencoes lpc=graph.getEdge(ciclo.get(k),ciclo.get(k+1));
                      Comparator <Pretencao> comparator; 
                      comparator= (p1,p2)->Long.compare(p1.getData(),p2.getData());
                      Pretencao pc=lpc.pretencoes.stream().min(comparator).get();
                      minCiclo=pc.getData();
                      //System.out.println("pedido mais antigo do "+k+"º ciclo :"+minCiclo);
                     }  
                      if(minCiclo<minData){
                          minData=minCiclo;
                          ciclo_A_resolver=ciclo;
                      }
                  }
                  System.out.println("--------Trocas na uc:" +uc+ "----");
                    for(int k=0;k<ciclo_A_resolver.size()-1;++k){
                        LPretencoes pAr=graph.getEdge(ciclo_A_resolver.get(k),ciclo_A_resolver.get(k+1));
                        Comparator <Pretencao> comparator; 
                        comparator= (p1,p2)->Long.compare(p1.getData(),p2.getData());
                        Pretencao pc=pAr.pretencoes.stream().min(comparator).get();
                        System.out.println("{aluno:"+pc.getMecanografico()+" ,data: "+pc.getData()+ " ,origem:" +pc.getOrigem()+" ,destino:"+pc.getDestino()+" }");
                    }
                }
                else{
                  System.out.println("--------Trocas na uc:" +uc+ "----");
                ciclo_A_resolver=maiorCiclos.get(0);
                ciclo_A_resolver.add(ciclo_A_resolver.get(0));
                for(int k=0;k<ciclo_A_resolver.size()-1;++k){
                 LPretencoes pAr=graph.getEdge(ciclo_A_resolver.get(k),ciclo_A_resolver.get(k+1));
                 Comparator <Pretencao> comparator; 
                 comparator= (p1,p2)->Long.compare(p1.getData(),p2.getData());
                 Pretencao pc=pAr.pretencoes.stream().min(comparator).get();
                 System.out.println("{aluno:"+pc.getMecanografico()+" ,data: "+pc.getData()+ " ,origem:" +pc.getOrigem()+" ,destino:"+pc.getDestino()+" }");
                }
                }
            }
            else{
            //Não existem ciclos no grafo 
                System.out.println("Nao existem ciclos no Grafo a serem resolvidos!!!!");
            }
           }
           
    }
}
