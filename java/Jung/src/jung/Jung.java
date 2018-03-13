/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jung;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import static java.util.stream.Collectors.toList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Samuel
 */
public class Jung {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, JSONException {
/* Ler e guardar ficheiro JSON num objeto JSON */
        Scanner s= new Scanner(new FileReader("../../../estruturaDados.txt"));
        StringBuilder sb = new StringBuilder();

        while(s.hasNext()){
         sb.append(s.next());
        }
        s.close();
        JSONObject obj = new JSONObject(sb.toString());
        /* Ler e guardar ficheiro JSON num objeto JSON */
        
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
                Integer data=(Integer) aluno.get("data");
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
            DirectedSparseGraph<String,LPretencoes> graph;
            graph =new DirectedSparseGraph<>(LPretencoes.class);
            for(String turno : turnos){
                graph.addVertex(turno);
            }
            for(Pretencao aresta: arestas){
                LPretencoes lPreten=graph.getEdge(aresta.getOrigem(),aresta.getDestino());
                if(lPreten!=null){
                    graph.getEdge(aresta.getOrigem(),aresta.getDestino()).pretencoes.add(aresta);
                }
                else{
                    LPretencoes novaLp= new LPretencoes();
                    novaLp.pretencoes.add(aresta);
                    graph.addEdge(novaLp,aresta.getOrigem(),aresta.getDestino(),EdgeType.DIRECTED);
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
                List<String> ciclo_A_resolver= new ArrayList<>();

                if(maiorCiclos.size()>1){
                 //Há Empate//   
                    Integer minData=Integer.MAX_VALUE;
                    for(List<String> ciclo: maiorCiclos){
                        ciclo.add(ciclo.get(0));
                        Integer minDataCiclo=Integer.MAX_VALUE;
                        
                        
                        for(int k=0;k<ciclo.size()-1;++k){
                            LPretencoes lpc = graph.getEdge(ciclo.get(k),ciclo.get(k+1));
                            Comparator <Pretencao> comparator; 
                            comparator= (p1,p2)->Integer.compare(p1.getData(),p2.getData());
                            Pretencao pc=lpc.pretencoes.stream().min(comparator).get();
                            int minDataAresta= pc.getData();
                            if(minDataAresta < minDataCiclo){
                                minDataCiclo = minDataAresta;
                            }
                            //System.out.println("pedido mais antigo do "+k+"º ciclo :"+minCiclo);
                        }
                        
                        if(minDataCiclo<minData){
                            minData=minDataCiclo;
                            ciclo_A_resolver=ciclo;
                        }
                    }
                    System.out.println("--------Trocas na uc:" +uc+ "----");
                    for(int k=0;k<ciclo_A_resolver.size()-1;++k){
                        LPretencoes pAr=graph.getEdge(ciclo_A_resolver.get(k),ciclo_A_resolver.get(k+1));
                        Comparator <Pretencao> comparator; 
                        comparator= (p1,p2)->Integer.compare(p1.getData(),p2.getData());
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
                        comparator= (p1,p2)->Integer.compare(p1.getData(),p2.getData());
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
