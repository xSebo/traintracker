package com.example.applicationone.trains;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

public class TrainService {

    public String getServiceUid() {
        return serviceUid;
    }

    public SimpleDateFormat getRunDate() {
        return runDate;
    }

    public int getPlatform() {
        return platform;
    }

    public boolean isPlatformConfirmed() {
        return platformConfirmed;
    }

    public boolean isPlatformChanged() {
        return platformChanged;
    }

    public int getBookedArrival() {
        return bookedArrival;
    }

    public int getActualArrival() {
        return actualArrival;
    }

    public int getBookedDeparture() {
        return bookedDeparture;
    }

    public int getActualDeparture() {
        return actualDeparture;
    }

    private String serviceUid; // serviceUid
    private SimpleDateFormat runDate; // runDate

    private int platform; // platform
    private boolean platformConfirmed; // platformConfirmed
    private boolean platformChanged; // platformChanged

    private int bookedArrival; // locationDetails -> gbttBookedArrival
    private int actualArrival; // locationDetails -> realtimeArrival
    private int bookedDeparture; // locationDetails -> gbttBookedDeparture
    private int actualDeparture; // locationDetails -> realtimeDeparture

    private boolean bus = false;

    private String finalDestination;

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    private String to;
    private String from;
    private boolean soonest;

    public ArrayList<TrainService> getLaterTrains() {
        return laterTrains;
    }

    private ArrayList<TrainService> laterTrains = new ArrayList<>();

    @Override
    public String toString() {
        String result = "Arrives(origin): " + bookedArrival + " -> ETA: " + actualArrival +
                "\nDeparts(origin): " + bookedDeparture + " -> ETA: " + actualDeparture +
                "\nPlatform: " + platform + " Confirmed? " + platformConfirmed;
        return result;
    }

    public String toListView() {
        String result = from + " -> " + to + "\nArrives(origin): " + bookedArrival + " -> ETA: " + actualArrival +
                "\nDeparts(origin): " + bookedDeparture + " -> ETA: " + actualDeparture +
                "\nPlatform: " + platform + " Confirmed? " + platformConfirmed + "\n" +
                "Terminates at: " + finalDestination;
        return result;
    }

    public String toLaterView(){
        String result = "Arrives(origin): " + bookedArrival + " -> ETA: " + actualArrival +
                "\nDeparts(origin): " + bookedDeparture + " -> ETA: " + actualDeparture +
                "\nPlatform: " + platform + " Confirmed? " + platformConfirmed + "\n" +
                "Terminates at: " + finalDestination;
        return result;
    }

    private TrainService generateTrain(TrainService train, JSONArray o, int index){
        JSONObject o2 = o.getJSONObject(index);
        JSONObject nextTrainLocationInfo = o2.getJSONObject("locationDetail");
        if (o2.getString("serviceType").equalsIgnoreCase("bus")) {
            train.serviceUid = o2.getString("serviceUid");
            train.runDate = new SimpleDateFormat(o2.getString("runDate"));
            train.platform = 0;
            train.platformChanged = false;
            train.platformConfirmed = true;
            train.bus = true;
        } else {
            train.serviceUid = o2.getString("serviceUid");
            train.runDate = new SimpleDateFormat(o2.getString("runDate"));
            try {
                train.platform = nextTrainLocationInfo.getInt("platform");
                train.platformChanged = nextTrainLocationInfo.getBoolean("platformChanged");
                train.platformConfirmed = nextTrainLocationInfo.getBoolean("platformConfirmed");
            }catch(JSONException e){
                train.platform = 0;
                train.platformChanged = false;
                train.platformConfirmed = false;
            }
        }
        try {
            train.finalDestination = o2.getJSONArray("destination").getJSONObject(0).getString("description");
        } catch (JSONException e) {
            train.finalDestination = nextTrainLocationInfo.getJSONArray("destination").getJSONObject(0).getString("description");
        }
        try {
            train.bookedArrival = nextTrainLocationInfo.getInt("gbttBookedArrival");
            train.actualArrival = nextTrainLocationInfo.getInt("realtimeArrival");
        } catch (Exception e) { //This occurs when a service starts at that station, since there is no arrival.
            train.bookedArrival = 0;
            train.actualArrival = 0;
        }
        train.bookedDeparture = nextTrainLocationInfo.getInt("gbttBookedDeparture");
        try {
            train.actualDeparture = nextTrainLocationInfo.getInt("realtimeDeparture");
        } catch (Exception e) {
            train.actualDeparture = bookedDeparture;
        }
        return train;
    }

    public void reload() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                String userName = "rttapi_xSebo";
                String password = "a377076c21bab1e0afc4922a76b0beda2925e0e5";
                JSONArray o = Unirest.get("https://api.rtt.io/api/v1/json/search/" + from + "/to/" + to)
                        .basicAuth(userName, password)
                        .asJson().getBody().getObject().getJSONArray("services");
                for(int i = 0; i<o.length(); i++) {
                    if(i == 0) {
                        generateTrain(this, o, i);
                    }else{
                        TrainService train = new TrainService();
                        train.to = this.to;
                        train.from = this.from;
                        generateTrain(train, o, i);
                        laterTrains.add(train);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
    }

    private TrainService(){}

    public TrainService(String from, String to, boolean soonest) throws InterruptedException {
        this.from = from;
        this.to = to;
        this.soonest = soonest;
        reload();

        ServiceArray.addTrain(this);
    }
}
