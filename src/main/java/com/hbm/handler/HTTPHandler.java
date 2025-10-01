package com.hbm.handler;

import com.hbm.HBMsNTM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPHandler {

    public static List<String> capsule = new ArrayList<>();
    public static List<String> tipOfTheDay = new ArrayList<>();
    public static boolean newVersion = false;
    public static String versionNumber = "";

    public static void loadStats() {

        Thread versionChecker = new Thread("NTM Version Checker") {

            @Override
            public void run() {
                try {
                    loadVersion();
                    loadSoyuz();
                    loadTips();
                } catch(IOException e) {
                    HBMsNTM.LOGGER.warn("Version checker failed!");
                }
            }

        };

        versionChecker.start();
    }

    private static void loadVersion() throws IOException {

        URL github = URI.create("https://raw.githubusercontent.com/HbmMods/Hbm-s-Nuclear-Tech-GIT/master/src/main/java/com/hbm/lib/RefStrings.java").toURL();
        BufferedReader in = new BufferedReader(new InputStreamReader(github.openStream()));

        HBMsNTM.LOGGER.info("Searching for new versions...");
        String line;

        while((line = in.readLine()) != null) {

            if(line.contains("String VERSION")) {

                int begin = line.indexOf('"');
                int end = line.lastIndexOf('"');

                String sub = line.substring(begin + 1, end);

                newVersion = !HBMsNTM.VERSION.equals(sub);
                versionNumber = sub;
                HBMsNTM.LOGGER.info("Found version {}", sub);
                break;
            }
        }

        HBMsNTM.LOGGER.info("Version checker ended.");
        in.close();
    }

    private static void loadSoyuz() throws IOException {

        URL github = URI.create("https://gist.githubusercontent.com/HbmMods/a1cad71d00b6915945a43961d0037a43/raw/soyuz_holo").toURL();
        BufferedReader in = new BufferedReader(new InputStreamReader(github.openStream()));

        String line;
        while((line = in.readLine()) != null) capsule.add(line);
        in.close();
    }

    private static void loadTips() throws IOException {

        URL github = URI.create("https://gist.githubusercontent.com/HbmMods/a03c66ba160184e12f43de826b30c096/raw/tip_of_the_day").toURL();
        BufferedReader in = new BufferedReader(new InputStreamReader(github.openStream()));

        String line;
        while((line = in.readLine()) != null) tipOfTheDay.add(line);
        in.close();
    }
}