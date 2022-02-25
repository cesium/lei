/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo;

import java.util.ArrayList;

/**
 *
 * @author Samuel
 */
public class Stack {
    private ArrayList<String> stack;
    
    public Stack(){
        stack = new ArrayList<>();
    }
    
    public Stack(ArrayList<String> stack){
        this.stack = new ArrayList<>();
        for(String x : stack){
            this.stack.add(x);
        }
    }
    
    public Stack(Stack x){
        this.stack = x.getStack();
    }

    public ArrayList<String> getStack() {
        ArrayList<String> ret = new ArrayList<>();
        for(String x : this.stack)
            ret.add(x);
        return ret;
    }
    
    public String top(){
        return stack.get(stack.size()-1);
    }
    
    public void push(String s){
        stack.add(s);
    }
    
    public void pop(){
        if(!this.empty())
            stack.remove(stack.size()-1);
    }
    
    public boolean empty(){
        return stack.size() == 0;
    }
    
    public int length(){
        return stack.size();
    }
    
    public String toString(){
        return stack.toString();
    }
}
