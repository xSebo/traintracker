package com.example.applicationone.trains;

import java.text.SimpleDateFormat;

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

    public void reload() throws InterruptedException {
        //System.out.println("Refreshing service with UID: " + this.serviceUid);

        final JSONObject[] nextTrainLocationInfo = {new JSONObject()};
        Thread thread = new Thread(() -> {
            try {
                String userName = "rttapi_xSebo";
                String password = "a377076c21bab1e0afc4922a76b0beda2925e0e5";
                //System.out.println("https://api.rtt.io/api/v1/json/search/" + from + "/to/" + to);
                JSONArray o = Unirest.get("https://api.rtt.io/api/v1/json/search/" + from + "/to/" + to)
                        .basicAuth(userName, password)
                        .asJson().getBody().getObject().getJSONArray("services");
                JSONObject o2;
                if (soonest) {
                    o2 = o.getJSONObject(0);
                } else {
                    o2 = o.getJSONObject(0); //TODO -> Implement fetch-all/time-specific fetches.
                }
                if(o2.getString("serviceType").equalsIgnoreCase("bus")){
                    this.serviceUid = o2.getString("serviceUid");
                    this.runDate = new SimpleDateFormat(o2.getString("runDate"));
                    this.platform = 0;
                    this.platformChanged = false;
                    this.platformConfirmed = true;
                    this.bus = true;
                }else {
                    nextTrainLocationInfo[0] = o2.getJSONObject("locationDetail");
                    this.serviceUid = o2.getString("serviceUid");
                    this.runDate = new SimpleDateFormat(o2.getString("runDate"));
                    this.platform = nextTrainLocationInfo[0].getInt("platform");
                    this.platformChanged = nextTrainLocationInfo[0].getBoolean("platformChanged");
                    this.platformConfirmed = nextTrainLocationInfo[0].getBoolean("platformConfirmed");
                }
                try {
                    this.finalDestination = o2.getJSONArray("destination").getJSONObject(0).getString("description");
                } catch (JSONException e) {
                    this.finalDestination = nextTrainLocationInfo[0].getJSONArray("destination").getJSONObject(0).getString("description");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();

        try {
            this.bookedArrival = nextTrainLocationInfo[0].getInt("gbttBookedArrival");
            this.actualArrival = nextTrainLocationInfo[0].getInt("realtimeArrival");
        } catch (Exception e) { //This occurs when a service starts at that station, since there is no arrival.
            this.bookedArrival = 0;
            this.actualArrival = 0;
        }
        this.bookedDeparture = nextTrainLocationInfo[0].getInt("gbttBookedDeparture");
        try {
            this.actualDeparture = nextTrainLocationInfo[0].getInt("realtimeDeparture");
        }catch (Exception e){
            this.actualDeparture = bookedDeparture;
        }
    }

    public TrainService(String from, String to, boolean soonest) throws InterruptedException {
        this.from = from;
        this.to = to;
        this.soonest = soonest;
        reload();
        ServiceArray.addTrain(this);
    }
}
