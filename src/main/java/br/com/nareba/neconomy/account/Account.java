package br.com.nareba.neconomy.account;

public class Account {
    private String playerName;
    private Double money;
    public Account(String playerName, Double money)   {
        this.playerName = playerName;
        this.money = money;
    }
    public String getPlayerName()   {
        return this.playerName;
    }
    public Double getMoney()   {
        return this.money;
    }
    public void setPlayerName(String playername)   {
        this.playerName = playername;
    }
    public void setMoney(Double money)   {
        this.money = money;
    }
}
