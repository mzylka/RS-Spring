package com.rs.app.dto;

import java.util.List;

public record AppnewsWrapper(
        Appnews appnews
){
    public record Appnews(
            List<SteamNewsDTO> newsitems
    ){
        public record SteamNewsDTO(
                String title,
                String url,
                String contents,
                long date,
                int appid
        ){}
    }
}
