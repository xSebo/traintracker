package com.example.applicationone.trains;

import java.text.SimpleDateFormat;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
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

    @Override
    public String toString(){
        String result = "Arrives(origin): "+bookedArrival+" -> ETA: "+actualArrival+
                "\nDeparts(origin): "+bookedDeparture+" -> ETA: "+actualDeparture+
                "\nPlatform: "+platform+ " Confirmed? "+platformConfirmed;
        return result;
    }



    public TrainService(String from, String to, boolean soonest) throws InterruptedException {

        final JSONObject[] nextTrainLocationInfo = {new JSONObject()};

        Thread thread = new Thread(() -> {
            try {
                String userName = "rttapi_xSebo";
                String password = "a377076c21bab1e0afc4922a76b0beda2925e0e5";
                JSONArray o = Unirest.get("https://api.rtt.io/api/v1/json/search/"+from+"/to/"+to)
                        .basicAuth(userName, password)
                        .asJson().getBody().getObject().getJSONArray("services");
                JSONObject o2;
                if(soonest) {
                    o2 = o.getJSONObject(0);
                }else{
                    o2 = o.getJSONObject(0); //TODO -> Implement fetch-all/time-specific fetches.
                }
                //System.out.println(o2.toString());
                nextTrainLocationInfo[0] = o2.getJSONObject("locationDetail");
                this.serviceUid = o2.getString("serviceUid");
                this.runDate = new SimpleDateFormat(o2.getString("runDate"));
                this.platform = nextTrainLocationInfo[0].getInt("platform");
                this.platformChanged = nextTrainLocationInfo[0].getBoolean("platformChanged");
                this.platformConfirmed = nextTrainLocationInfo[0].getBoolean("platformConfirmed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();

        try {
            this.bookedArrival = nextTrainLocationInfo[0].getInt("gbttBookedArrival");
            this.actualArrival = nextTrainLocationInfo[0].getInt("realtimeArrival");
        }catch(Exception e){ //This occurs when a service starts at that station, since there is no arrival.
            this.bookedArrival = 0;
            this.actualArrival = 0;
        }
        this.bookedDeparture = nextTrainLocationInfo[0].getInt("gbttBookedDeparture");
        this.actualDeparture = nextTrainLocationInfo[0].getInt("realtimeDeparture");

    }
}
