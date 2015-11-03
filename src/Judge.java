import AI.Card;
import AI.Move;
import AI.PlayerAI;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by devin on 11/2/15.
 */
public class Judge {
    private PlayerAI myAI;
    private PlayerAI playerAI;
    private Stack<Card> deck;

    private int gamesPlayed;
    private int gamesWon;

    public Judge(File file) {
        this.deck = initDeck();
        this.myAI = new RandomAI();
        this.playerAI = loadPlayerAi(file);
    }

    private Stack<Card> initDeck() {
        Stack<Card> deck = new Stack<>();

        deck.add(Card.KING);
        deck.add(Card.QUEEN);
        deck.add(Card.JACK);
        Collections.shuffle(deck);

        return deck;
    }

    private PlayerAI loadPlayerAi(File file) {
        PlayerAI result = null;

        try {
            ClassLoader playerAiLoader = URLClassLoader.newInstance(new URL[]{file.toURL()});
            result = (PlayerAI) playerAiLoader.loadClass("AI.PlayerAI").newInstance();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void dealCards() {
        playerAI.setCard(deck.pop());
        myAI.setCard(deck.pop());
    }

    public void startGame() throws Exception {
        final double RATE = .5;

        int round = 0;
        boolean isPlaying = true;
        double coinFlip = Math.random();
        PlayerAI currentPlayer = (coinFlip > RATE) ? myAI : playerAI;

        dealCards();
        Move move = currentPlayer.play();

        while (isPlaying && round < 3) {
            currentPlayer = (currentPlayer == myAI) ? playerAI : myAI;
            Move nextMove = currentPlayer.play(move);

            if (move == Move.CHECK) {
                if (nextMove == Move.CHECK) {
                    //TODO: show down
                    isPlaying = false;
                    showDown();
                } else if (nextMove != Move.BET) {
                    throw new Exception("You can not do that");
                }
            } else if (move == Move.BET) {
                if (nextMove == Move.FOLD) {
                    // other player wins
                    isPlaying = false;
                } else if (nextMove == Move.CALL) {
                    //TODO: show down
                    isPlaying = false;
                    showDown();
                } else {
                    throw new Exception("You cant do that");
                }
            }

            round++;
            move = nextMove;
        }

        gamesPlayed++;
    }

    private void showDown() {
        int playerAiValue = playerAI.showHand().compareTo(myAI.showHand());

        if (playerAiValue >= 1) {
            gamesWon++;
        }
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }
}
