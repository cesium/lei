
package grafos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Grafos {

    /**
     * @param args the command line arguments
     * @throws org.json.JSONException
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws JSONException, FileNotFoundException {
           
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
            ArrayList<String> turnos = new ArrayList<>();
            ArrayList<Pretencao> arestas = new ArrayList<>();
            String uc = keys.getString (i); // tenho a key//
            //System.out.println(uc);
            JSONArray trocas = obj.getJSONArray(uc);
            for(int j=0;j<trocas.length();j++){ //iterar sobre as pretencoes de troca do turno // 
                JSONObject troca = trocas.getJSONObject(j); 
                String turnoO=troca.get("from_shift_id").toString();
                String turnoD=troca.get("to_shift_id").toString();
                String idTroca=troca.get("id").toString();
                Integer data=(Integer) troca.get("created_at");
                Pretencao p= new Pretencao(data, turnoO,turnoD,idTroca);

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
            graph =new DefaultDirectedWeightedGraph<>(LPretencoes.class);
            for(String turno : turnos){
                graph.addVertex(turno);
            }
            for(Pretencao aresta: arestas){
                LPretencoes Lpreten=graph.getEdge(aresta.getFrom_shift_id(),aresta.getTo_shift_id());
                if(Lpreten!=null){
                    graph.getEdge(aresta.getFrom_shift_id(),aresta.getTo_shift_id()).pretencoes.add(aresta);
                }
                else{
                    LPretencoes novaLp= new LPretencoes();
                    novaLp.pretencoes.add(aresta);
                    graph.addEdge(aresta.getFrom_shift_id(),aresta.getTo_shift_id(),novaLp);
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
                List<List<String>> maioresCiclos= listaCiclos.stream().filter(x->x.size()==longest).collect(toList());
                
                System.out.println("Maiores ciclos:"+maioresCiclos.toString());
                
                for(List<String> ciclo : maioresCiclos){
                    ciclo.add(ciclo.get(0));
                }
                
                List<String> ciclo_A_resolver= new ArrayList<>();

                if(maioresCiclos.size()>1){
                 //Há Empate//
                    int minData =0;
                    HashMap<Integer,List<List<String>>> minDataCiclos = new HashMap<>();
                    boolean desempatado = false;
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
                System.out.println("--------Trocas na uc:" +uc+ "----");
                for(int k=0;k<ciclo_A_resolver.size()-1;++k){
                    LPretencoes pAr=graph.getEdge(ciclo_A_resolver.get(k),ciclo_A_resolver.get(k+1));
                    Comparator <Pretencao> comparator; 
                    comparator= (p1,p2)->Integer.compare(p1.getCreated_at(),p2.getCreated_at());
                    Pretencao pc=pAr.pretencoes.stream().min(comparator).get();
                    System.out.println(pc.getId());
                    System.out.println("\tcreated_at: "+pc.getCreated_at()+ "\n\tfrom_shift_id:" +pc.getFrom_shift_id()+"\n\tto_shift_id:"+pc.getTo_shift_id());
                }
                
            }
            else{
            //Não existem ciclos no grafo 
                System.out.println("Nao existem ciclos no Grafo a serem resolvidos!!!!");
            }
        }
           
    }

    private static int getMinDataCiclo(DefaultDirectedWeightedGraph<String, LPretencoes> graph, List<String> ciclo,int minData) {
        int minDataCiclo = Integer.MAX_VALUE;
        for(int k=0;k<ciclo.size()-1;++k){
            LPretencoes lpc = graph.getEdge(ciclo.get(k),ciclo.get(k+1));
            Comparator <Pretencao> comparator = (p1,p2)->Integer.compare(p1.getCreated_at(),p2.getCreated_at());
            List<Pretencao> pc=lpc.pretencoes.stream().sorted(comparator).collect(Collectors.toList());
            int minDataAresta = Integer.MAX_VALUE;
            boolean naoConta = false;
            for(int i=0; i<pc.size() ; i++){
                int dataAresta = pc.get(i).getCreated_at();
                if(dataAresta <= minData){
                    naoConta=true;
                    break;
                }
                if(dataAresta > minData &&
                   dataAresta < minDataAresta ){
                    minDataAresta = dataAresta;
                }
            }
            if(!naoConta && minDataAresta < minDataCiclo){
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
