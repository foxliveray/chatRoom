package org.example;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        File file = new File("example.txt");
        if(file.exists()){
            System.out.println("yes!");
        }
        System.out.println( file.getAbsolutePath() );
        String path = App.class.getClass().getResource("/").getPath();
        System.out.println(path);
    }
}
