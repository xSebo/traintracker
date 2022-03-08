package com.example.applicationone.trains;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceArray {
    public static List<TrainService> getActiveTrains() {
        return activeTrains;
    }

    public static List<String> getStrings(){
        List<String> strings = new ArrayList<>();
        for(TrainService t:activeTrains){
            strings.add(t.toListView());
        }
        return strings;
    }

    private static List<TrainService> activeTrains = new ArrayList<>();

    public static void addTrain(TrainService train){
        activeTrains.add(train);
    }

    public static void addTrains(TrainService[] trains){
        for(TrainService train : trains){
            activeTrains.add(train);
        }
    }

    public static TrainService findByIndex(int i){
        return activeTrains.get(i);
    }

    public static void clearTrains(){
        System.out.println(activeTrains);
        List<TrainService> tempTrains = new ArrayList();
        for(TrainService t:activeTrains){
            tempTrains.add(t);
        }
        for(TrainService t:tempTrains){
            activeTrains.remove(t);
        }
    }

    public static void removeTrain(TrainService train){
        for(TrainService t:activeTrains){
            if(t.getServiceUid().equalsIgnoreCase(train.getServiceUid())){
                activeTrains.remove(t);
            }
        }
    }

    public static boolean alreadyExists(String[] fromTo){
        for(TrainService t:activeTrains){
            if(t.getFrom().equalsIgnoreCase(fromTo[0]) &&
            t.getTo().equalsIgnoreCase(fromTo[1])){
                return true;
            }
        }
        return false;
    }

    public static void updateTrains() throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        for(TrainService t:activeTrains){
            threads.add(new Thread(() -> {
                try {
                    t.reload();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        for(Thread t:threads){
            t.start();
        }
        for(Thread t:threads){
            t.join();
        }


    }

    public static Optional<TrainService> findByUid(String uid){
        for(TrainService t:activeTrains){
            if(t.getServiceUid().equalsIgnoreCase(uid)){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public static Optional<TrainService> findByFromTo(String from, String to){
        for(TrainService t:activeTrains){
            if(t.getFrom().equalsIgnoreCase(from) && t.getTo().equalsIgnoreCase(to)){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public static void updateTrain(TrainService train) throws InterruptedException {
        for(TrainService t:activeTrains){
            if(t.getServiceUid().equalsIgnoreCase(train.getServiceUid())){
                t.reload();
            }
        }
    }


}
