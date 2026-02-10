package com.rs.app.mappers;

import com.rs.app.domain.entities.Game;
import com.rs.app.dto.game.GameDTO;
import com.rs.app.dto.game.GameRequest;
import com.rs.app.dto.game.GameSimpleDTO;

public interface GameMapper{
    Game fromRequest(GameRequest gameRequest);
    GameDTO toDto(Game game);
    GameSimpleDTO toSimpleDto(Game game);
}
