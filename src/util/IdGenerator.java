/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Random;

/**
 *
 * @author Admin
 */
public class IdGenerator {
        private static final Random RANDOM = new Random();

     
    public static String generateId(int year) {
        int randomPart = 10000 + RANDOM.nextInt(90000); 
        return String.format("%d%d", year, randomPart);
    }
}
