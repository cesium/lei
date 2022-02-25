/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos;

import java.util.ArrayList;

/**
 *
 * @author Bruno
 */
public class LPretencoes {
    private ArrayList<Pretencao> pretencoes;
    private int minData;
    private Pretencao minPretencao ;
    public LPretencoes() {
        pretencoes = new ArrayList<>();
        minData = Integer.MAX_VALUE;
        minPretencao = null;
    }
    
    public String toString(){
        String ret="[";
        for(Pretencao p : pretencoes){
            ret += p.toString();
        }
        ret+="]";
        return ret;
    }
    
    public void addPretencao(Pretencao p){
        pretencoes.add(p);
        if(p.getCreated_at() < minData){
            minData = p.getCreated_at();
            minPretencao = p;
        }
    }
    
    public int getMinData(){
        return minData;
    }
    
    public Pretencao getMinPretencao(){
        return minPretencao;
    }
    
    
}
