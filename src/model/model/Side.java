/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.model;

import java.util.stream.Stream;

/**
 *
 * @author Adrien
 */
public enum Side
{
    LEFT(0),
    RIGHT(1);
    
    Side(int value)
    {
        this.value = value;
    }
    
    private final int value;

    public int getValue()
    {
        return this.value;
    }
    
    public static Side getFromValue(String value)
    {
        return getFromValue(Integer.parseInt(value));
    }
    public static Side getFromValue(int value)
    {
        return Stream.of(Side.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElse(LEFT);
    }
}
