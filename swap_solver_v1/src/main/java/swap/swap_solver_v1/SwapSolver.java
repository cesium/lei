/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swap.swap_solver_v1;

/**
 *
 * @author Bruno
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import static java.util.stream.Collectors.toList;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SwapSolver {
    public static void main(String[] args) throws JSONException, FileNotFoundException {
        long antes = System.nanoTime();
        /* Ler e guardar ficheiro JSON num objeto JSON */
        Scanner s= new Scanner(new FileReader("../../../estruturaDados_v2.txt"));
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
            ArrayList<ExchangeRequest> arestas = new ArrayList<>();
            
            String uc = keys.getString (i); // key---> correponde à cadeira//
            //System.out.println(uc);
            JSONArray trocas = obj.getJSONArray(uc);
            
            DefaultDirectedWeightedGraph<String,ERList> graph;
            graph =new DefaultDirectedWeightedGraph<>(ERList.class);
            
            for(int j=0;j<trocas.length();j++){ 
                //iterar sobre as pretencoes de troca do turno // 
                JSONObject troca = trocas.getJSONObject(j); 
                String turnoO=troca.get("from_shift_id").toString();
                String turnoD=troca.get("to_shift_id").toString();
                String idTroca=troca.get("id").toString();
                Integer data=(Integer) troca.get("created_at");
                ExchangeRequest p= new ExchangeRequest(data, turnoO,turnoD,idTroca);

                if(!graph.vertexSet().contains(turnoO)){
                    graph.addVertex(turnoO);
                }
                if(!graph.vertexSet().contains(turnoD)){
                    graph.addVertex(turnoD);
                }
                if(!arestas.contains(p)){
                    arestas.add(p);
                }            
            }
            
            
            for(ExchangeRequest aresta: arestas){
                ERList Lpreten=graph.getEdge(aresta.getFrom_shift_id(),aresta.getTo_shift_id());
                if(Lpreten!=null){
                    graph.getEdge(aresta.getFrom_shift_id(),aresta.getTo_shift_id()).addExchangeRequest(aresta);
                }
                else{
                    ERList novaLp= new ERList();
                    novaLp.addExchangeRequest(aresta);
                    graph.addEdge(aresta.getFrom_shift_id(),aresta.getTo_shift_id(),novaLp);
                }     
            }     
            TarjanSimpleCycles tsc;
            tsc=new TarjanSimpleCycles(); 
            tsc.setGraph(graph);
            List<List<String> > listaCiclos= tsc.findSimpleCycles();
            
            if(listaCiclos.size()>0){
            //Existem ciclos no grafo
                int longest=listaCiclos.stream().mapToInt(List::size).max().orElse(-1);
                List<List<String>> maioresCiclos= listaCiclos.stream().filter(x->x.size()==longest).collect(toList());
                System.out.println("--------Trocas na uc:" +uc+ "----");
                System.out.println("Maiores ciclos:"+maioresCiclos.toString());
                
                for(List<String> ciclo : maioresCiclos){
                    ciclo.add(ciclo.get(0));
                }
                
                List<String> ciclo_A_resolver;

                if(maioresCiclos.size()>1){
                 //Há Empate//
                    int minData =0;
                    HashMap<Integer,List<List<String>>> minDataCiclos;
                    boolean desempatado;
                    int maxIters = graph.vertexSet().size() -1;
                    int iter = 0;
                    do{
                        minDataCiclos = new HashMap<>();
                        for(List<String> ciclo: maioresCiclos){
                            int minDataCiclo=getMinDataCiclo(graph,ciclo,minData);
                            if(minDataCiclos.containsKey(minDataCiclo)){
                                minDataCiclos.get(minDataCiclo).add(ciclo);
                            }
                            else{
                                List<List<String>> listaDeCiclos = new ArrayList<>();
                                listaDeCiclos.add(ciclo);
                                minDataCiclos.put(minDataCiclo, listaDeCiclos);
                            }
                        }
                        minData = Collections.min(minDataCiclos.keySet());
                        //System.out.println(minDataCiclos);
                        //new Scanner(System.in).next();
                        desempatado = minDataCiclos.get(minData).size() == 1;
                        if(!desempatado){
                            maioresCiclos = minDataCiclos.get(minData);
                        }
                    }
                    while(!desempatado && (++iter != maxIters));
                    if(iter == maxIters) System.out.println("\nmaxIters atingido!!!\n");
                    ciclo_A_resolver = minDataCiclos.get(minData).get(0);
                    
                }
                else{
                    ciclo_A_resolver = maioresCiclos.get(0);
                }
                
                for(int k=0;k<ciclo_A_resolver.size()-1;++k){
                    ERList pAr=graph.getEdge(ciclo_A_resolver.get(k),ciclo_A_resolver.get(k+1));
                    ExchangeRequest pc=pAr.getMinExchangeRequest();
                    System.out.println(pc.toString());
                }
                
            }
            else{
            //Não existem ciclos no grafo 
                System.out.println("Nao existem ciclos no Grafo a serem resolvidos!!!!");
            }
        }
        long depois = System.nanoTime();
        System.out.println("Tempo decorrido: " + (depois - antes)/1000000 + " milissegundos");
    }

    private static int getMinDataCiclo(DefaultDirectedWeightedGraph<String, ERList> graph, List<String> ciclo,int minData) {
        int minDataCiclo = Integer.MAX_VALUE;
        for(int k=0;k<ciclo.size()-1;++k){
            ERList lpc = graph.getEdge(ciclo.get(k),ciclo.get(k+1));
            int minDataAresta = lpc.getMinData();
            if( minDataAresta > minData && minDataAresta < minDataCiclo){
                minDataCiclo = minDataAresta;
            }
        }
        if(minDataCiclo == Integer.MAX_VALUE){
            System.out.println("ESTE CICLO NAO TEM NENHUMA ARESTA COM MENOR PESO!!!");
            return minData;
        }
        return minDataCiclo;
    }
}
