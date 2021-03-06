package com.texas.holdem.service;

import com.texas.holdem.elements.players.Player;
import com.texas.holdem.elements.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;

@Service
public class PlayerService {
    @Autowired
    RoomService roomService;

    /**
     * <h3>Zwiększanie betu gracza</h3>
     * @param room pokój
     * @param player gracz który betuje
     * @param bet wysokość zakładu którą dokładamy do zakłądu
     */
    public void setBet(Room room, Player player, int bet) {
        var notPassed = room.getNotPassedPlayers();
        var maxBet = notPassed.stream()
                .max(Comparator.comparingInt(Player::getBet))
                .map(n -> n.getBet()).orElse(0);

        if (player.isPass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player passed");
        if (!player.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't your turn");
        if (player.getBudget() < bet)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your budget is too low");
        if (player.getBet() + bet < maxBet && player.getBudget() - bet != 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your bet is too low");

        var allins = notPassed.stream().filter(n -> n.isAllIn()).count();

        betHelper(player, room, bet);
        var table = room.getTable();
        if(player.getBet() > table.getMaxBet())
            table.setMaxBet(player.getBet());

        if (player.getBudget() == 0 && allins == 0) {
            player.setLastAction("All in");
            notPassed.forEach(n -> n.setCheck(false));
            player.setAllIn(true);
        } else if(player.getBudget() == 0){
            player.setLastAction("All in");
            player.setAllIn(true);
        }else if (player.getBet() > maxBet) {
            notPassed.forEach(n -> n.setCheck(false));
            player.setLastAction("raise");
        } else
            player.setLastAction("call");

        player.setCheck(true);
        room.nextTurn(player);
    }

    /**
     * <h3>Pasowanie</h3>
     * @param room pokój
     * @param player gracz który chce spasować
     */
    public void pass(Room room, Player player) {

        if (player.isPass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player already passed");
        if (!player.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It isn't your turn");

        player.setPass(true);
        player.setCheck(false);
        player.setLastAction("fold");

        room.nextTurn(player);

    }

    /**
     * <h3>Zmiana gotowaści gracza</h3>
     * @param room pokój
     * @param player gracz który znienia gotowość
     */
    public void setReady(Room room, Player player) {
        player.setReady(!player.isReady());

        var players = room.getPlayers();
        var ready = players.stream().filter(n -> n.isReady()).count();

        if (ready == players.size())
            roomService.startRound(room);

    }

    /**
     * <h3>Funkcja pomocznicza dla betowania</h3>
     * @param player gracz
     * @param room pokój
     * @param bet zakłau
     */
    private void betHelper(Player player, Room room, int bet) {
        player.addBet(bet);
        player.subBudget(bet);
        player.addWholeRoundBet(bet);
        room.addCoinsInRound(bet);
    }

}
