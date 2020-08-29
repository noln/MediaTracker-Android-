package com.example.mediatracker20.ui.search.data;

import model.jsonreaders.AniListReader;
import model.jsonreaders.AnimeReader;
import model.jsonreaders.MalReader;
import model.requests.AniList;
import model.requests.AnimeRequest;
import model.requests.MyAnimeList;

public class SourceChooser {

    public static AnimeRequest getAnimeReq(String source) {
        switch(source) {
            case "MAL Anime":
                return  new MyAnimeList();
            case "AniList Anime":
                return new AniList();
        }
        return null;
    }

    public static AnimeReader getAnimeRead(String source) {
        switch(source) {
            case "MAL Anime":
                return  new MalReader();
            case "AniList Anime":
                return new AniListReader();
        }
        return null;
    }
}
