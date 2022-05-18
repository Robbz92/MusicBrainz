package com.example.demo.services;

import com.example.demo.entities.Albums;
import com.example.demo.entities.MusicBrainz;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusicBrainzService {
    final HttpComponentsClientHttpRequestFactory factory;

    public MusicBrainzService(){
        // Lägger koden här istället för att skapa en ny anslutnings-instans för CodeArt vid varje iteration.
        factory = new HttpComponentsClientHttpRequestFactory();
        final CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .disableContentCompression() // Snabbade upp körningen minimalt
                .build();
        factory.setHttpClient(httpClient);
    }

    public MusicBrainz getMusicBrainArtist(String id) throws JSONException {
        // Utgår från denna URL struktur för alla band.
        String musicBrainApi = "http://musicbrainz.org/ws/2/artist/" + id + "?&fmt=json&inc=url-rels+release-groups";
        RestTemplate restTemplate = new RestTemplate();

        // Konverterar om responsen till en map
        Map response = restTemplate.getForObject(musicBrainApi, Map.class);

        // kollar specifikt i listan release-groups för att få ut id, first_release-date och title
        List<Map> releaseGroupList = (List<Map>) response.get("release-groups");

        // Går igenom datan i "release-groups"
        List<Albums> albumsList = releaseGroupList.parallelStream()
                // Filterar först för där primary-type och first-release-date inte är null
                // Sedan skapar jag ett objekt för varje album jag hittar. Samlar sedan all data till en ny lista för albumsList
                .filter((album) -> album.get("primary-type") != null && album.get("first-release-date") != null)
                .map((album) -> new Albums(
                        (String) album.get("id"),
                        (String) album.get("title"),
                        (String) coverArt((String) album.get("id")), // spara en för varje album
                        (String) album.get("first-release-date"))).collect(Collectors.toList()); //Collect samlar all data till en ny lista

        String bandName = (String) response.get("name");
        bandName = bandName.replace(" ", "_").toLowerCase();

        return new MusicBrainz(
                (String) response.get("name"),
                (String) response.get("id"),
                (String) wikipedia(bandName), // Hämtar wikipedia datan om bandet
                albumsList
        );
    }

    public Object wikipedia(String phrase) throws JSONException {
        String wikipediaApi = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles="+phrase+"&20";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(wikipediaApi, String.class);

        JSONObject jsonObject = new JSONObject(response);
        String key = findNextKey(jsonObject.getJSONObject("query").getJSONObject("pages"));

        // Returnerar wikipedia datan som finns om bandet.
        return jsonObject.getJSONObject("query").getJSONObject("pages").getJSONObject(key).getString("extract").replaceAll("\\<.*?>", "") ;
    }

    public String findNextKey(JSONObject jsonObject) throws JSONException {
        return jsonObject.names().getString(0);
    }

    public Object coverArt(String id) {
        String coverArtApit = "http://coverartarchive.org/release-group/" + id;

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(factory);

            Map<String, String> params = new HashMap<String, String>();

            ResponseEntity<Map> response = restTemplate.postForEntity(coverArtApit, params, Map.class);
            String strResponse = response.getBody().get("images").toString();

            // start index för image-url
            String strResponseImageUrl = strResponse.substring(strResponse.indexOf("small="));

            // slut index för image-url
            int endIndex = strResponseImageUrl.indexOf(".jpg");

            // Plockar ut image-url
            String strImage = strResponseImageUrl.substring(0, endIndex);

            // rensar bort text från url och returnerar den till albumet.
            return strImage.replace("small=", "") + ".jpg";

        }catch(HttpClientErrorException e){
            return "No cover art found for release group";
        }
    }
}
