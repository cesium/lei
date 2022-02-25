/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Samuel
 */
public class FBFeed {
    private List<FBPost> posts;
    
    public FBFeed(List<FBPost> posts){
        this.posts = new ArrayList<>();
        for(FBPost x : posts){
            this.posts.add(x.clone());
        }
    }
    
    public int nrPosts(String user){
        long count;
        count = posts.stream().filter(x -> x.getUsername().equals(user)).count();
        return (int)count;
        
    }
    
    public List<FBPost> postsOf(String user){
        ArrayList<FBPost> ret = new ArrayList<>();
        posts.forEach(x ->{
            if (x.getUsername().equals(user))
                ret.add(x.clone());
        });
        return ret;
    }
    
    public List<FBPost> postsOf(String user, LocalDateTime inicio, LocalDateTime fim){
        return posts.stream().filter(x -> x.getUsername().equals(user) &&
                                     x.getData() >= inicio && x.getData() <= fim).
                collect(Collectors.toCollection(ArrayList :: new));       
    }
    
    public FBPost getPost(int id){
        ArrayList<FBPost> res = posts.stream().filter(x -> x.getId() == id).map(FBPost::clone).collect(Collectors.toCollection(ArrayList :: new));
        if(res.size() > 0) return res.get(0);
        else return null;
    }
        
}
