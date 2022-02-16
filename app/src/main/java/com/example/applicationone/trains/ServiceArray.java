package com.example.applicationone.trains;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceArray {
    public static ArrayList<TrainService> getActiveTrains() {
        return activeTrains;
    }

    public static List<String> getStrings(){
        List<String> strings = new ArrayList<>();
        for(TrainService t:activeTrains){
            strings.add(t.toListView());
        }
        return strings;
    }

    private static ArrayList<TrainService> activeTrains = new ArrayList<>();

    public static void addTrain(TrainService train){
        activeTrains.add(train);
    }

    public static void addTrains(TrainService[] trains){
        for(TrainService train : trains){
            activeTrains.add(train);
        }
    }

    public static void removeTrain(TrainService train){
        for(TrainService t:activeTrains){
            if(t.getServiceUid().equalsIgnoreCase(train.getServiceUid())){
                activeTrains.remove(t);
            }
        }
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
