/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.najah.ai_tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author RayaThawabe
 */
public class Shipment implements SAEntityInterface{
    public ArrayList<Node>[] trucksPaths;
    public ArrayList<Node>[] displayPaths;
    public ArrayList<Node> nodesList;
    public int numberOfTrucks;
    public int truckCapacity;
    private boolean pathConstructed = false;
    
    Shipment(int numberOfTrucks, int truckCapacity){
        this.numberOfTrucks = numberOfTrucks;
        this.truckCapacity = truckCapacity;
        this.nodesList = new ArrayList<>();
        trucksPaths = new ArrayList[this.numberOfTrucks];
        displayPaths = new ArrayList[this.numberOfTrucks];
        for(int i = 0; i < this.numberOfTrucks; i++){
            trucksPaths[i] = new ArrayList<>();
            displayPaths[i] = new ArrayList<>();
        }
    }
    
    public boolean isAllFulfilled(){
        for(Node p : nodesList){
            if(p.isStart)
                continue;
            if(p.currentCapacity != p.capacity)
                return false;
        }
        return true;
    }
    
    public float calculateTotalDistance(){
        float total = 0;
        if(!pathConstructed)
            this.constructCurrentTrucksDisplayPath();
        for(ArrayList<Node> paths: displayPaths){
            for(int i = 0; i < paths.size() - 1; i++){
                Node current = paths.get(i);
                Node next = paths.get(i+1);
                total += calculateDistance(current, next);
            }
        }
        return total;
    }
    
    private Node getStartingNode(){
        return nodesList.stream().filter(p -> p.isStart).findFirst().get();
    }
    
    private ArrayList<Node> constructTruckPath(ArrayList<Node> path, Node startNode){
        if(path == null){
            path = new ArrayList<>();
        }
        List<Node> filteredPath = path.stream().filter(p -> !p.isStart).toList();
        ArrayList<Node> newPath = new ArrayList<>();
        newPath.add(startNode);
        int currentCapacity = this.truckCapacity;
        boolean returnToStart = true;
        for(Node node: filteredPath){
            returnToStart = true;
            int nodeEmptyCapacity = node.capacity - node.currentCapacity;
            if(nodeEmptyCapacity == 0)
                continue;
            if(nodeEmptyCapacity > currentCapacity){
                node.calculateCurrentVisits(truckCapacity);
                double remainingVisits = node.numberOfVisitsNeeded - node.numberOfCurrentVisits;
                double fractionVisit = remainingVisits % 1;
                if(node.partialVisit || fractionVisit != 0 && fractionVisit * this.truckCapacity > currentCapacity){
                    newPath.add(startNode);
                    currentCapacity = this.truckCapacity;
                }else{
                    node.partialVisit = true;
                }
            }
            if(nodeEmptyCapacity > currentCapacity){
                node.currentCapacity += currentCapacity;
                currentCapacity = 0;
            }else{
                currentCapacity = currentCapacity - nodeEmptyCapacity;
                node.currentCapacity = node.capacity;
            }
            newPath.add(node);
            if(currentCapacity == 0){
                returnToStart = false;
                newPath.add(startNode);
                currentCapacity = this.truckCapacity;
            }
        }
        if(returnToStart)
            newPath.add(startNode);
        return newPath;
    }

    
    public void constructCurrentTrucksDisplayPath(){
        Node startNode = this.getStartingNode();
        this.resetCapacities();
        for(int i = 0; i < this.numberOfTrucks; i++){
            displayPaths[i] = constructTruckPath(trucksPaths[i], startNode);
        }
        this.pathConstructed = true;
    }
    
    private void resetCapacities(){
        nodesList.forEach(p ->{ 
            p.currentCapacity = 0;
            p.calculateCurrentVisits(truckCapacity);
            p.calculateNeededVisits(truckCapacity);
            });
    }
    
    public void initializePath(){
        if(nodesList.size() < 2)
            return;
        Node startNode = this.getStartingNode();
        if(startNode == null)
            return;
        ArrayList<Node> warehouses = new ArrayList<>(nodesList.stream().filter(p -> !p.isStart).toList());
        Collections.shuffle(warehouses);
        int currentTruck = 0;
        for(Node p : warehouses){
            p.currentCapacity = 0;
            p.numberOfVisitsNeeded = Math.ceil((double)p.capacity / this.truckCapacity);
            while(p.numberOfVisitsNeeded > 0){
                trucksPaths[currentTruck].add(p);
                currentTruck++;
                p.numberOfVisitsNeeded--;
                if(currentTruck >= this.numberOfTrucks)
                    currentTruck = 0;
            }
            p.calculateNeededVisits(truckCapacity);
        }
        this.constructCurrentTrucksDisplayPath();
    }
    
    @Override
    public Shipment clone(){
        Shipment cloned = new Shipment(this.numberOfTrucks, this.truckCapacity);
        // Copy nodes
        for(Node node : this.nodesList){
            Node toBeAdded = new Node(node);
            cloned.nodesList.add(toBeAdded);
        }
        
        for(int i = 0; i < this.trucksPaths.length; i++){
            ArrayList<Node> path = trucksPaths[i];
            for(Node nodeInPath: path){
                int index = nodesList.indexOf(nodeInPath);
                if(index == -1)
                    continue;
                Node newNode = cloned.nodesList.get(index);
                cloned.trucksPaths[i].add(newNode);
            }
        }
        cloned.resetCapacities();
        cloned.constructCurrentTrucksDisplayPath();
        
        return cloned;
    }
    
    public Shipment generateNeighborShipment(){
        Shipment nextState =  this.clone();
        Random random = new Random();
        int firstIndex = random.nextInt(this.trucksPaths.length);
        int secondIndex = trucksPaths.length - firstIndex - 1; 
        
        ArrayList<Node> firstPath = nextState.trucksPaths[firstIndex];
        Collections.shuffle(firstPath);
        ArrayList<Node> secondPath = nextState.trucksPaths[secondIndex];
        Collections.shuffle(secondPath);
        int nodesSwapped = Math.max(1, random.nextInt(Math.max(firstPath.size(), secondPath.size())));
        for(int i = 0; i < nodesSwapped; i++){
            int index1 = getRandomIndex(firstPath);
            int index2 = getRandomIndex(secondPath);
            swapNodes(firstPath, secondPath, index1, index2);
        }
        nextState.constructCurrentTrucksDisplayPath();
        return nextState;
    }
    
    private int getRandomIndex(List path){
        if(path.isEmpty())
            return -1;
        Random random = new Random();
        if(random.nextBoolean()){
            return -1;
        }else{
            return random.nextInt(path.size());
        }
    }
    
    private void swapNodes(ArrayList<Node> list1, ArrayList<Node> list2, int index1, int index2) {
        if(index1 >= 0 && index2 >= 0){
            Node temp = list1.get(index1);
            list1.set(index1, list2.get(index2));
            list2.set(index2, temp);
        }else if(index1 >= 0){
            list2.add(list1.get(index1));
            list1.remove(list1.get(index1));
        }else if(index2 >= 0){
            list1.add(list2.get(index2));
            list2.remove(list2.get(index2));
        }
    }
    
    private float calculateDistance(Node c1,Node c2){
        return (float) Math.sqrt(Math.pow(c1.x-c2.x,2)+Math.pow(c1.y-c2.y,2));
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Trucks:\n");
        for(int i = 0; i < this.numberOfTrucks; i++){
            builder.append("Truck ").append(i).append(":\n");
            ArrayList<Node> path = this.displayPaths[i];
            if(path.size() <= 2){
                builder.append("Truck not utilized.\n");
                continue;
            }
            for(Node n : path){
                builder.append(n.isStart ? "Start" : n.id).append("-");
            }
            builder.append("\n");
        }
        builder.append("Nodes:\n");
        for(Node n : this.nodesList){
            builder.append(n.toString()).append("\n");
        }
        builder.append("All nodes are fulfilled?");
        if(this.isAllFulfilled()){
            builder.append("Yes");
        }else{
            builder.append("No");
        }
        builder.append("\n");
        return builder.toString();
    }

    @Override
    public SAEntityInterface generateNeighbor() {
        return this.generateNeighborShipment();
    }

    @Override
    public float score() {
        return this.calculateTotalDistance();
    }

    @Override
    public void copyTo(SAEntityInterface source) {
        this.copy((Shipment)source);
    }
    
    
    public void copy(Shipment source){
        this.displayPaths = source.displayPaths;
        this.trucksPaths = source.trucksPaths;
        this.nodesList = source.nodesList;
        this.numberOfTrucks = source.numberOfTrucks;
        this.truckCapacity = source.truckCapacity;
    }
}
