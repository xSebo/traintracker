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

    private static final List<Station> cachedList = new ArrayList<>();
    private static final List<Station> cachedList2 = new ArrayList<>();


    public static List<String> getStationNames() {
        List<String> names = new ArrayList<>();
        for (Station s : cachedList) {
            names.add(s.getName());
        }
        //System.out.println(names.toString());
        return names;
    }

    public static List<Station> getStations(){
/*
        List<Station> tempList = new ArrayList<>();
        tempList.addAll(cachedList);
        return tempList;
*/
        return cachedList;
    }
    public static List<Station> getStations2(){
        return cachedList2;
    }

    /**
     * A static search method to fetch a three digit station code list based off of a search term.
     *
     * @param - Search term
     * @return List of Station objects
     * @throws InterruptedException
     */
    public static void initialise() throws InterruptedException {
        cachedList.clear();
        final List<String>[] tempList = new List[]{new ArrayList<>()};
            Thread thread = new Thread(() -> {
                String response = Unirest.get("https://pastebin.com/raw/c9KmgWR7")
                        .asString()
                        .getBody();
                tempList[0] = Arrays.asList(response.split(","));
            });
            thread.start();
            thread.join();
            //System.out.println(tempList[0].toString());

            for (int i = 0; i < tempList[0].size(); i += 2) {
                cachedList.add(new Station(tempList[0].get(i), tempList[0].get(i + 1)));

            }
            cachedList2.addAll(cachedList);

        for(int i = 0; i < cachedList.size(); i++){
            String tempString = cachedList.get(i).getName();
            if(tempString.startsWith("\n\r")){
                tempString = tempString.substring(2);
                cachedList.get(i).setName(tempString);
            }
            if(tempString.startsWith("\r\n")){
                tempString = tempString.substring(2);
                cachedList.get(i).setName(tempString);
            }
            if(tempString.startsWith("\\r")){
                tempString = tempString.substring(1);
                cachedList.get(i).setName(tempString);

            }
            if(tempString.startsWith("\\n")){
                tempString = tempString.substring(1);
                cachedList.get(i).setName(tempString);
            }

        }
        return;


    }
}
