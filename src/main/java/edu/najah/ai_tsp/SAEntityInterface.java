/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.najah.ai_tsp;

/**
 *
 * @author RayaThawabe
 */
public interface SAEntityInterface{
    public SAEntityInterface generateNeighbor();
    public float score();
    public SAEntityInterface clone();
    public void copyTo(SAEntityInterface source);
}
