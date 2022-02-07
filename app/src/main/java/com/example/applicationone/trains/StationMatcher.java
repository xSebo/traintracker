package com.example.applicationone.trains;

import com.example.applicationone.R;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;

public class StationMatcher {


    /**
     *
     * @param query - Search term
     * @return 3 Digit station code(s)
     * @throws InterruptedException
     */
    public static List<String> searchStation(String query) throws InterruptedException {

        final List<String>[] stationList = new List[]{new ArrayList<>()};

        Thread thread = new Thread(() -> {
            stationList[0] = Arrays.asList(Unirest.get("https://pastebin.com/raw/c9KmgWR7")
                    .asString().getBody().split(","));
        });
        thread.start();
        thread.join();

        List<String> list = new ArrayList<>();

        for(int i = 0; i< stationList[0].size(); i+=2){
            if(stationList[0].get(i).toLowerCase().contains(query.toLowerCase())){
                //System.out.println(stationList[0].get(i) + ", " + stationList[0].get(i+1));
                list.add(stationList[0].get(i));
                list.add(stationList[0].get(i+1));
            }
        }
        return list;

    }
}
