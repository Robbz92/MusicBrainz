package com.example.demo.controllers;

import com.example.demo.entities.MusicBrainz;
import com.example.demo.services.MusicBrainzService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MusicBrainzController {

    @Autowired
    private MusicBrainzService musicBrainzService;

    @GetMapping("/MBID/{id}")
    public MusicBrainz getMusicBrainArtist(@PathVariable String id) throws JSONException {
        return musicBrainzService.getMusicBrainArtist(id);
    }
}
