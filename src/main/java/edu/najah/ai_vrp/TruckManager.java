/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.najah.ai_vrp;

import java.util.ArrayList;

class CoolingFunction1 extends CoolingFunction{
    @Override
    public int apply(int currentTemp, int iteration) {
        return (int) ((int)currentTemp/(Math.log(iteration+3)));
    }
} // create new class inherited from coolingfunction to create new cooling functions

public class TruckManager {
    public Shipment currentShipment;
    public int truckCapacity;
    public int numberOfTrucks;
    private boolean pathMade = false;
    public int initialTemp = 10000;
    public int finalTemp = 900;
    public SimulatedAnnealing<Shipment> SA;
    
    public TruckManager(int truckCapacity, int numberOfTrucks){
        this.truckCapacity = truckCapacity;
        this.numberOfTrucks = numberOfTrucks;
        this.currentShipment = new Shipment(numberOfTrucks, truckCapacity);
    }
    
    public void reset(){
        this.currentShipment = new Shipment(numberOfTrucks, truckCapacity);
    }
    
    public float calculateCurrentDistance(){
        return currentShipment.calculateTotalDistance();
    }
    
    // this needs refactoring
    public void FindOptimizedPaths(int maxIter){
       
        SA = SimulatedAnnealing.<Shipment>createInstance();
        this.currentShipment = (Shipment) SA.setInitialTemp(this.initialTemp).
                setFinalTemp(this.finalTemp).
                setMaximumIterations(maxIter).
                setCurrentState(currentShipment).
                setCoolingFunction(new CoolingFunction1()). // this to set cooling function
                findOptimizedSolution();
        
    }

  
    public void constructPath(){
        this.currentShipment.initializePath();
        pathMade = true;
    }
    
    public boolean isAllFulfilled(){
        return this.currentShipment.isAllFulfilled();
    }
    
    public void setNodesList(ArrayList<Node> nodes){
        if(this.currentShipment == null)
            return;
        currentShipment.nodesList = nodes;
    }
    
    public ArrayList<Node>[] getDisplayPath(){
        return this.currentShipment.displayPaths;
    }
    
//    public static void main(String[] args){
//        TruckManager truckManager = new TruckManager(100, 4);
//        
//        Node startNode = new Node(0, 0);
//        startNode.isStart = true;
//        
//        Node node1 = new Node(40, 40);
//        node1.capacity = 40;
//        node1.isStart = false;
//        node1.id = 1;
//        
//        Node node2 = new Node(1000, 40);
//        node2.capacity = 130;
//        node2.isStart = false;
//        node2.id = 2;
//        
//        Node node3 = new Node(40, 320);
//        node3.capacity = 150;
//        node3.isStart = false;
//        node3.id = 3;
//        
//        Node node4 = new Node(40, 900);
//        node4.capacity = 70;
//        node4.isStart = false;
//        node4.id = 4;
//        
//        Node node5 = new Node(1250, 1245);
//        node5.capacity = 80;
//        node5.isStart = false;
//        node5.id = 5;
//        
//        ArrayList<Node> nodes = new ArrayList<>();
//        
//        nodes.add(startNode);
//        nodes.add(node1);
//        nodes.add(node2);
//        nodes.add(node3);
//        nodes.add(node4);
//        nodes.add(node5);
//        truckManager.setNodesList(nodes);
//
//        truckManager.constructPath();
//        System.out.println(truckManager.calculateCurrentDistance());
//        System.out.println(
//                truckManager.isAllFulfilled()
//        );
//        System.out.println(truckManager.currentShipment.toString());
//        truckManager.FindOptimizedPaths(0);
//        System.out.println(truckManager.calculateCurrentDistance());
//        System.out.println(truckManager.isAllFulfilled());
//        System.out.println(truckManager.currentShipment.toString());
//    }
}
