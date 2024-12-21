/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.najah.ai_tsp;

import javax.swing.JOptionPane;

/**
 *
 * @author RayaThawabe
 */
public class Node {
    public int id;
    public int x;
    public int y;
    public int capacity;
    public int currentCapacity = 0;
    public boolean isStart = false;
    public double numberOfVisitsNeeded = 0;
    public double numberOfCurrentVisits = 0;
    public boolean partialVisit = false;
    public Node next;
    

    public void calculateNeededVisits(int truckCapacity){
        this.numberOfVisitsNeeded = (double) this.capacity / truckCapacity;
    }
    
    public void calculateCurrentVisits(int truckCapacity){
        this.numberOfCurrentVisits = (double) this.currentCapacity / truckCapacity;
    }

    public void readCapacity(){
        this.capacity = Integer.parseInt(JOptionPane.showInputDialog("Enter capacity of the new delivery point"));
    }

    Node(int x,int y){
        this.x = x;
        this.y = y;
        this.capacity = 0;
        this.next = null;
        this.id = 0;
    }
    
    Node(Node x){
        this.x = x.x;
        this.y = x.y;
        this.capacity = x.capacity;
        this.isStart = x.isStart;
        this.id = x.id;
    }
    
    @Override
    public String toString(){
        return "Node "+this.id+": "+this.currentCapacity+"/"+this.capacity;
    }
}
