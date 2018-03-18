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
    int created_at;
    String from_shift_id;
    String to_shift_id;
    String id;
    
    public Pretencao(int created_at, String from_shift_id, String to_shift_id,String id){
        this.created_at=created_at;
        this.from_shift_id=from_shift_id;
        this.to_shift_id=to_shift_id;
        this.id=id;
    }
    
    /*GET's*/
    String getFrom_shift_id(){return this.from_shift_id;}
    String getTo_shift_id(){return this.to_shift_id;}
    int getCreated_at(){return this.created_at;}
    String getId(){return this.id;}
    
   /* Boolean equals(Pretencao a, Pretencao b){
       return (a.data == b.data && a.destino==b.destino &&  a.origem==b.origem); 
    }
    */
    /* SET'S */
    
    void setFrom_shift_id(String from_shift_id){this.from_shift_id=from_shift_id;}
    void setTo_shift_id(String to_shift_id){this.to_shift_id=to_shift_id;}
    void setCreated_at(int data){this.created_at=created_at;}
    void setId(String mecanografico){this.id=id;}
    
    public String toString(){
        return "{from: " + this.from_shift_id +
               ", to: " + this.to_shift_id + 
               ", created_at: " + this.created_at +
               ", id: " + this.id + "}";
    }
    
}
