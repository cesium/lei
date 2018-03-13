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
public class Pretencao {
    Number data;
    String origem;
    String destino;
    
    
    public Pretencao(Number data, String origem, String destino){
        this.data=data;
        this.origem=origem;
        this.destino=destino;
    }
    
    /*GET's*/
    String getOrigem(){return this.origem;}
    String getDestino(){return this.destino;}
    Number getData(){return this.data;}
    
   /* Boolean equals(Pretencao a, Pretencao b){
       return (a.data == b.data && a.destino==b.destino &&  a.origem==b.origem); 
    }
    */
    /* SET'S */
    
    void setOrigem(String origem){this.origem=origem;}
    void setDestion(String destino){this.destino=destino;}
    void setData(Number data){this.data=data;}
    
}
