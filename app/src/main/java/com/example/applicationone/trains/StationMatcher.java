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

    private static List<String>[] cachedList = new List[]{new ArrayList<>()};

    /**
     * A static search method to fetch a three digit station code list based off of a search term.
     * @param query - Search term
     * @return List of Station objects
     * @throws InterruptedException
     */
    public static List<Station> searchStation(String query) throws InterruptedException {

        if(cachedList[0].size() == 0) {
            Thread thread = new Thread(() -> {
                cachedList[0] = Arrays.asList(Unirest.get("https://pastebin.com/raw/c9KmgWR7")
                        .asString().getBody().split(","));
            });
            thread.start();
            thread.join();
        }

        List<Station> searchResults = new ArrayList<>();

        for(int i = 0; i< cachedList[0].size(); i+=2){
            if(cachedList[0].get(i).toLowerCase().contains(query.toLowerCase())){
                //System.out.println(stationList[0].get(i) + ", " + stationList[0].get(i+1));
                Station station = new Station(cachedList[0].get(i),cachedList[0].get(i+1));

                searchResults.add(station);
            }
        }

        return searchResults;

    }
}
