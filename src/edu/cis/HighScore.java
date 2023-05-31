package edu.cis;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HighScore {
    private ArrayList<Score> scores;
    private final String SCORES_FILE = "scores.json";

    public HighScore() {
        scores = new ArrayList<>();
        loadScores();
    }

    public ArrayList<Score> getScores() {
        return scores;
    }

    public void addScore(Score score) {
        scores.add(score);
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score s1, Score s2) {
                return Long.compare(s2.getTime(), s1.getTime());
            }
        });

        if (scores.size() > 5) {
            scores.remove(scores.size() - 1);
        }

        saveScores();
    }

    public void clearScores() {
        scores.clear();

        try (FileWriter writer = new FileWriter(SCORES_FILE)) {
            writer.write("[]");
        } catch (IOException e) {
            System.err.println("Error clearing scores: " + e.getMessage());
        }
    }

    private void loadScores() {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(SCORES_FILE)) {
            Object scoreObj = parser.parse(reader);

            if (scoreObj instanceof JSONObject) {
                JSONObject obj = (JSONObject) scoreObj;
                String name = (String) obj.get("name");
                if (name != null) {
                    // Use name
                }

                Long time = (Long) obj.get("time");
                if (time != null) {
                    long timeVal = time.longValue();
                    // Use timeVal
                }
            } else if (scoreObj instanceof JSONArray) {
                JSONArray scoreArray = (JSONArray) scoreObj;

                for (Object obj : scoreArray) {
                    JSONObject score = (JSONObject) obj;
                    String name = (String) score.get("name");
                    long time = (long) score.get("time");
                    scores.add(new Score(name, time));
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error loading scores: " + e.getMessage());
        }
    }

    private void saveScores() {
        JSONArray scoreArray = new JSONArray();

        for (Score score : scores) {
            JSONObject scoreObj = new JSONObject();
            scoreObj.put("name", score.getName());
            scoreObj.put("time", score.getTime());
            scoreArray.add(scoreObj);
        }

        try (FileWriter writer = new FileWriter(SCORES_FILE)) {
            writer.write(scoreArray.toJSONString());
        } catch (IOException e) {
            System.err.println("Error saving scores: " + e.getMessage());
        }
    }
}