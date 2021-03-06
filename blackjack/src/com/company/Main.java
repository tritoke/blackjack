package com.company;


import javax.swing.*;
import java.util.*;

public class Main {
    private static List<card> deck = new ArrayList<>();
    private static final String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
    private static final String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private static final String[] cardinalNumbers = {"st", "nd", "rd", "th"};
    private static HashMap<String, Integer> pointValues = new HashMap<>();
    private static int numDecks;


    public static void main(String[] args) {
        for (int i = 0; i < ranks.length; i++){ //this for loop sets up the HashMap
            if (i < 10){
                pointValues.put(ranks[i], (i+1));
            } else {
                pointValues.put(ranks[i], 10);
            }
        }


        JOptionPane.showMessageDialog(
                null,
                "Hi, welcome to blackjack.\n" +
                        "By Sam Leonard",
                "Welcome",
                JOptionPane.PLAIN_MESSAGE
        );
        JOptionPane.showMessageDialog(
                null,
                "The aim of the game is to get the value of your hand to 21\n" +
                        "each card has a different value between 1 and 11\n" +
                        "number cards are worth the number on the card\n" +
                        "and picture cards like jack/queen/king are worth 10\n",
                "Rules",
                JOptionPane.PLAIN_MESSAGE
        );
        JOptionPane.showMessageDialog(
                null,
                "Aces can be worth 1 or 11.\n" +
                        "If you are dealt an ace you will be asked if you want to play\n" +
                        "it as 1 or 11 after you have finished being dealt cards.",
                "Dealing with aces",
                JOptionPane.PLAIN_MESSAGE
        );
        JOptionPane.showMessageDialog(
                null,
                "in the case of a draw everyone who drew wins.\n",
                "Dealing with draws",
                JOptionPane.PLAIN_MESSAGE
        );

        String deckMessage = "how many decks do you want to use?";
        String numberOfDecks = JOptionPane.showInputDialog(deckMessage);
        numDecks = validateNumber(
                numberOfDecks,
                deckMessage,
                8,
                "you cannot play with more than 8 decks"
        );

        deckCreate(); //this function resets the deck array and must be done after numDecks is initialised

        String playingMessage = "how many people are playing?";
        String numPlaying =  JOptionPane.showInputDialog(playingMessage);
        int numPlayers = validateNumber(
                numPlaying,
                playingMessage,
                20,
                "you cannot play with more than 20 players"
        );

        ArrayList<player> players = new ArrayList<>();//this creates the array of players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new player()); //this is essential to initialize each object in the array
            players.get(i).setPlayerName(JOptionPane.showInputDialog("please enter the name of player " + (i + 1) + ":"));
        }

        do {
            int[] playerPoints = new int[numPlayers]; //this stores the scores of each player
            for (int i = 0; i < numPlayers; i++) {
                players.get(i).newRound();
                players.get(i).addCard(deck.remove(0));
                players.get(i).addCard(deck.remove(0));
            }

            for (int i = 0; i < numPlayers; i++) {
                int hit = 0;
                int valueOfHand;
                do {
                    valueOfHand = 0;
                    StringBuilder message = new StringBuilder();
                    message.append(players.get(i).getPlayerName());
                    message.append(" your cards are:\n");


                    int numAces = 0;
                    for (card c : players.get(i).getCards()) {
                        if (c.getRank().equals(ranks[0])) {
                            numAces++;
                        }
                        message.append(c.getName());
                        message.append("\n");
                        valueOfHand += pointValues.get(c.getRank());
                    }


                    if (valueOfHand < 21) {
                        if (numAces != 0) {
                            if (valueOfHand+10 < 21) {
                                message.append("this hand has a minimum value of: ");
                                message.append(valueOfHand);
                                message.append("\nand a maximum value of: ");
                                message.append(valueOfHand + 10);
                                message.append("\nwould you like another card?");
                            } else if (valueOfHand+10 == 21) {
                                message.append("this hand has a value of 21");
                                valueOfHand+=10;
                            } else {
                                message.append("this hand has a value of: ");
                                message.append(valueOfHand);
                                message.append("\nwould you like another card?");
                            }
                        } else {
                            message.append("this hand has a value of: ");
                            message.append(valueOfHand);
                            message.append("\nwould you like another card?");
                        }
                        hit = JOptionPane.showConfirmDialog(null, message.toString(), null, JOptionPane.YES_NO_OPTION);
                        if (hit == 0 && valueOfHand != 21) {
                            players.get(i).addCard(deck.remove(0));
                        }
                    }

                    if (valueOfHand == 21) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Congratulations you have blackJack!",
                                "Blackjack!",
                                JOptionPane.PLAIN_MESSAGE);
                        hit = 1;
                    } else if (valueOfHand > 21){
                        JOptionPane.showMessageDialog(
                                null,
                                "Unfortunately you have gone bust.",
                                "Bust!",
                                JOptionPane.PLAIN_MESSAGE);
                        hit = 1;
                    }
                } while (hit == 0);
                int numAces = 0;
                for (card c : players.get(i).getCards()) {
                    if (c.getRank().equals(ranks[0])) {
                        numAces++;
                    }
                }
                if (numAces != 0 && valueOfHand < 11) {
                    valueOfHand+=10;
                }
                if (valueOfHand > 21) {
                    valueOfHand = 0;
                }
                playerPoints[i] = valueOfHand;
            }
            int maxScore = 0;
            int maxScoreIndex = 0;
            ArrayList<Integer> topPlayers = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                if (playerPoints[i] > maxScore) {
                    maxScoreIndex = i;
                    maxScore = playerPoints[i];
                    topPlayers = new ArrayList<>();
                    topPlayers.add(i);
                } else if (playerPoints[i] == maxScore){
                    topPlayers.add(i);
                }
            }


            if (maxScore == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "everyone is bust, therefore the round is a draw",
                        "Bust!",
                        JOptionPane.PLAIN_MESSAGE
                );
            } else if (topPlayers.size() >= 2){
                StringBuilder winMessage = new StringBuilder("the highest scores were:\n");
                for (int winner:topPlayers) {
                    winMessage.append(players.get(winner).getPlayerName());
                    winMessage.append("\n");
                    players.get(winner).addWin();
                }
                winMessage.append("each with a score of ");
                winMessage.append(maxScore);
                JOptionPane.showMessageDialog(
                        null,
                        winMessage.toString(),
                        "Winner!",
                        JOptionPane.PLAIN_MESSAGE
                );
            } else {
                String winMessage =
                        "the player with the highest score was " + players.get(maxScoreIndex).getPlayerName() +
                                "\nWith a score of " + maxScore;
                JOptionPane.showMessageDialog(
                        null,
                        winMessage,
                        "Winner!",
                        JOptionPane.PLAIN_MESSAGE
                );
                players.get(maxScoreIndex).addWin();
            }
        } while (!quitCheck());

        StringBuilder leaderBoardMessage = new StringBuilder("scores:\n");

        int maxScore;
        int i = 0;
        do {
            maxScore = 0;
            ArrayList<player> topScores = new ArrayList<>();
            for (player p:players) {
                if (p.getNumWins() > maxScore) {
                    topScores = new ArrayList<>();
                    maxScore = p.getNumWins();
                    topScores.add(p);
                } else if (p.getNumWins() == maxScore) {
                    topScores.add(p);
                }
            }
            for (player p:topScores) {
                players.remove(p);
                leaderBoardMessage.append(i+1);
                if (i < 4){
                    leaderBoardMessage.append(cardinalNumbers[i]);
                } else {
                    leaderBoardMessage.append(cardinalNumbers[3]);
                }
                leaderBoardMessage.append(": ");
                leaderBoardMessage.append(p.getPlayerName());
                leaderBoardMessage.append(", ");
                leaderBoardMessage.append(p.getNumWins());
                leaderBoardMessage.append("\n");
                i++;
            }
        } while (players.size() != 0 && i < 21);

        JOptionPane.showMessageDialog(
                null,
                leaderBoardMessage.toString(),
                "Scoreboard",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private static boolean quitCheck() {
        int quitChoice = JOptionPane.showConfirmDialog(
                null,
                "do you want to play another round?",
                null,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (quitChoice == -1) {
            System.exit(0);
        }
        return quitChoice == 1;
    } //this function asks the user whether they want to play another round and
    //and forces them into a yes no decision

    private static int validateNumber(String numberString, String message, int maxVal, String warningMessage){
        boolean validNumber = false;
        int returnNum = 0;
        do{
            try { //this try catch statement is here so that the user is forced into entering a number such that they
                //  can't break the game by accident by typing five as it is an open text box
                returnNum = Integer.parseInt(numberString);
                if (returnNum == -1) {
                    System.exit(0);
                }
                if (returnNum <= maxVal && returnNum > 0) {
                    validNumber = true;
                } else {
                    JOptionPane.showMessageDialog(null, warningMessage);
                }
            } catch (NumberFormatException e){
                numberString = JOptionPane.showInputDialog(
                        "Please enter a number.\n" +
                                message,
                        "2");
            }
        } while (!validNumber);
        return returnNum;
    }

    private static void deckCreate(){
        deck = new ArrayList<>();
        int deckPosition = 0;
        for (int i = 0; i < numDecks; i++) {
            for (String s : suits) {
                for (String r : ranks) { //this nested for each loop fills the card[] deck with cards
                    deck.add(new card());
                    deck.get(deckPosition).init(s, r);
                    deckPosition++;
                }
            }
        }
        for (int j = 0; j < 50; j++){ //this shuffles the deck by randomly removing cards and placing them in a new deck
            ArrayList<card> shuffledDeck = new ArrayList<>(); //and it does this 50 times to shuffle it a lot
            while (deck.size() > 0) {
                int index = (int) (Math.random() * deck.size());
                shuffledDeck.add(deck.remove(index));
            }
            deck = shuffledDeck;
        }
    }
}


class player { //the cards assigned to each player
    private ArrayList<card> cards; //the maximum number of cards you can have without going bust is 11
    private String playerName;
    private int numWins = 0;

    public int getNumWins() {
        return numWins;
    }
    public void newRound(){
        cards = new ArrayList<>();
    } //this clears the ArrayList storing the cards so that the player
    //can receive a new hand
    public void addWin(){
        numWins++;
    }
    public ArrayList<card> getCards() {
        return cards;
    }
    public void addCard(card card) {
        this.cards.add(card);
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {

        this.playerName = playerName;

    }

}


class card {
    private String suit;
    private String rank;

    public void init(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }
    public String getRank() {
        return rank;
    }
    public String getName() {
        return rank + " of " + suit;
    }
}

