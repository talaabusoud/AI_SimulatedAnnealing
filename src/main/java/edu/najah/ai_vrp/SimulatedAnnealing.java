/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.najah.ai_vrp;

import java.util.Random;

public class SimulatedAnnealing<TEntity extends SAEntityInterface>{
    private static SimulatedAnnealing instance;
    private int initialTemp;
    private int finalTemp;
    public Integer currentTemp = 0;
    public boolean repaint = false;
    private TEntity bestState;
    private TEntity currentState;
    private TEntity nextState;
    private TEntity parentState;
    private CoolingFunction cooling;
    private final int delayTime = 0;
    
    private int maxIter;
    private SimulatedAnnealing(){
        
    }
    
    public static <T extends SAEntityInterface> SimulatedAnnealing getInstance(){
        return instance;
    }
    
    public static <T extends SAEntityInterface> SimulatedAnnealing createInstance(){
        if(instance == null)
            instance = new SimulatedAnnealing<T>();
        
        return instance;
    }

    
    public SimulatedAnnealing setCoolingFunction(CoolingFunction function){
        this.cooling = function;
        return this;
    }

    public SimulatedAnnealing setInitialTemp(int init){
        this.initialTemp = init;
        this.currentTemp = init;
        return this;
    }
    
    public SimulatedAnnealing setFinalTemp(int init){
        this.finalTemp = init;
        return this;
    }
    
    public SimulatedAnnealing setCurrentState(TEntity entity){
        this.currentState = entity;
        this.parentState = entity;
        return this;
    }
    
    public SimulatedAnnealing setMaximumIterations(int maxIter){
        this.maxIter = maxIter;
        return this;
    }
    public TEntity findOptimizedSolution(){
        try{
            int i = 0;
            bestState = (TEntity) currentState.clone();
            float bestDistance = bestState.score();
            TEntity current = (TEntity) currentState.clone();
            currentTemp = initialTemp;
            while(currentTemp >= finalTemp){
                // Child Generation and distances calculation
                //System.out.println(currentTemp); 
                
                nextState = (TEntity)current.generateNeighbor();
                float nextDistance = nextState.score();
                float currentDistance = current.score();
            

                float E = currentDistance - nextDistance;
               
                if(E > 0){
                    current = (TEntity) nextState.clone();
                    if(nextDistance < bestDistance){
                        bestState = (TEntity) nextState.clone();
                        bestDistance = nextDistance;
                        parentState.copyTo(bestState);
                        repaint = true;
                        System.out.println("Test2");
                    }
                }else{
                    Random rand = new Random();
                    float f1 = rand.nextFloat();
                    if(Math.exp(E/currentTemp) > f1){
                        current = (TEntity) nextState.clone();
                    }
                }
                i++;
                currentTemp = this.cooling.apply(initialTemp,i+1);
                if(i == maxIter){
                    break;
                }
                try {
                Thread.sleep(delayTime);    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bestState;
        }
        catch(Exception e){
            throw e;
        }
    }
}
