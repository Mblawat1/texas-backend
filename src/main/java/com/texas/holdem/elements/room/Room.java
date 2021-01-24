package com.texas.holdem.elements.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.texas.holdem.elements.cards.Deck;
import com.texas.holdem.elements.cards.HoleSet;
import com.texas.holdem.elements.players.Player;
import com.texas.holdem.elements.players.PlayerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class Room {
    String messageType;
    String id;
    ArrayList<Player> players;
    Table table;
    @JsonIgnore
    private int startingBudget = 5000;
    @JsonIgnore
    Deck deck;

    public Room(String id, Table table) {
        messageType = "roomStatus";
        this.id = id;
        this.players = new ArrayList<Player>();
        this.table = table;
        deck = new Deck();
    }

    /**
     * <h3>Dodawanie gracza do pokoju<h3/>
     * @param playerDTO gracz
     * @return id gracza
     */
    public int addPlayer(PlayerDTO playerDTO) {
        if(playerDTO.getAvatar() > 16 || playerDTO.getAvatar() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Avatar number is not in range");
        int newPlayerId;

        if (players.isEmpty())
            newPlayerId = 1;
        else
            newPlayerId = players.get(players.size() - 1).getId() + 1;
        var player = new Player(newPlayerId, playerDTO.getNickname(), startingBudget, playerDTO.getAvatar(), new HoleSet());
        players.add(player);

        if (player.getId() == players.get(0).getId())
            player.setStarting(true);
        return newPlayerId;
    }

    public void deletePlayer(int id) {
        players.removeIf(n -> n.getId() == id);
    }

    //jeśli nie ma gracza wywala exception

    /**
     * <h3>Szuka gracza o podanym id</h3>
     * @param id id gracza
     * @return Gracz
     * @throws ResponseStatusException z HttpStatus.NOT_FOUND jeśli gracza nie ma
     */
    public Player getPlayerOrThrow(int id) {
        return players.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    public void nextTurn(int playerId) {
        var activePlayer = getPlayerOrThrow(playerId);

        activePlayer.setActive(false);
        //szukam pierwszego nie spasowanego
        var lowerId = players.stream()
                .filter(n -> !n.isPass())
                .findFirst();
        //szukam pierwszego aktywnego z wyższym id
        var higherId = players.stream()
                .filter(n -> !n.isPass() && n.getId() > playerId)
                .findFirst();
        //jeśli jest z wyższym id to zmieniam jego
        Player pla;
        if (higherId.isPresent()) {
            pla = higherId.get();
            pla.setActive(true);
        } else if (lowerId.isPresent()) {
            pla = lowerId.get();
            pla.setActive(true);
        }
    }

    public void addCoinsInRound(int bet) {
        table.addCoinsInRound(bet);
    }

    public void nextStarting() {
        var optStartingPlayer = players.stream().filter(n -> n.isStarting()).findFirst();
        var startingPlayer = optStartingPlayer
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Starting player not found"));
        startingPlayer.setStarting(false);

        var higherPlayer = players.stream()
                .filter(n -> n.getId() > startingPlayer.getId() && n.getBudget() != 0)
                .findFirst();

        if (higherPlayer.isPresent())
            higherPlayer.get().setStarting(true);
        else
            players.get(0).setStarting(true);
    }

    @JsonIgnore
    public List<Player> getNotPassedPlayers(){
        return players.stream()
                .filter(n -> !n.isPass())
                .collect(Collectors.toList());
    }
}
