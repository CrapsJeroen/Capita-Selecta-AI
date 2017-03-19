package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class Data {
    List<Double> times = new ArrayList<Double>();
    List<Double> fitness = new ArrayList<Double>();
    List<Integer> generations = new ArrayList<Integer>();
    List<Integer> communities = new ArrayList<Integer>();
    static NumberFormat formatter = new DecimalFormat("#0.0000");

    
    public void addData(double time, double fitness, int generations, int communities){
        this.times.add(time);
        this.fitness.add(fitness);
        this.generations.add(generations);
        this.communities.add(communities);
    }
    
    public void printData(){
        System.out.print("Times: ");
        times.stream().forEach(t -> System.out.print(formatter.format(t) + " "));
        System.out.println();
        double avgTime = times.stream().reduce(0.0, (count, current) -> count + current) / times.size();
        
        System.out.print("Fitness: ");
        fitness.stream().forEach(t -> System.out.print(formatter.format(t) + " "));
        System.out.println();
        double avgFitness = fitness.stream().reduce(0.0, (count, current) -> count + current) / fitness.size();
        
        System.out.print("Gens: ");
        generations.stream().forEach(t -> System.out.print(t + " "));
        System.out.println();
        
        System.out.print("Coms: ");
        communities.stream().forEach(t -> System.out.print(t + " "));
        System.out.println();
        
        System.out.println("Avg Time: " + formatter.format(avgTime));
        System.out.println("Avg Fitness: " + formatter.format(avgFitness));
    }

}
