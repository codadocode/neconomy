package br.com.nareba.neconomy.account;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, Account> accounts;
    private String moneyType = "$";
    private final double initialValue = 1000.0;
    public AccountManager()   {
        this.accounts = new HashMap<String, Account>();
    }
    public AccountManager(Map<String, Account> accounts)   {
        this.accounts = accounts;
    }
    public boolean createNewAccount(String playerName)   {
        if (getAccount(playerName) == null)   {
            Account newAccount = new Account(playerName, initialValue);
            this.accounts.put(playerName, newAccount);
            return true;
        }
        return false;
    }
    public boolean deleteAccount(String playerName)   {
        Account account = getAccount(playerName);
        if (account != null)   {
            this.accounts.remove(playerName);
            if (getAccount(playerName) == null)   {
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean pay(String player1, String player2, Double value)   {
        Account account1 = getAccount(player1);
        Account account2 = getAccount(player2);
        if (account1 != null && account2 != null)   {
            if (hasMoney(player1, value))   {
                subValue(player1, value);
                addValue(player2, value);
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean accountExists(String playerName)   {
        if (this.accounts.containsKey(playerName))   {
            return true;
        }
        return false;
    }
    private Account getAccount(String playerName)   {
        if (accountExists(playerName))   {
            return this.accounts.get(playerName);
        }
        return null;
    }
    private boolean hasMoney(String playerName, Double value)   {
        Account account = getAccount(playerName);
        if (account != null)   {
            if (account.getMoney() >= value)   {
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean subValue(String playerName, Double value)   {
            Account playerAccount = getAccount(playerName);
            if (playerAccount != null)   {
                Double accountValue = playerAccount.getMoney();
                accountValue -= value;
                playerAccount.setMoney(accountValue);
                return true;
            }
            return false;
    }
    public boolean addValue(String playerName, Double value)   {
        Account playerAccount = getAccount(playerName);
        if (playerAccount != null)   {
            Double accountValue = playerAccount.getMoney();
            accountValue += value;
            playerAccount.setMoney(accountValue);
            return true;
        }
        return false;
    }
    public String getMoneyType()   {
        return this.moneyType;
    }
    public Double getAccountBalance(String playerName)   {
        Account account = getAccount(playerName);
        if (account != null)   {
            return account.getMoney();
        }
        return null;
    }
    public Map<String, Account> getAllAccounts()   {
        return this.accounts;
    }
    public boolean setAccountMoney(String playerName, Double value)   {
        Account account = getAccount(playerName);
        if (account != null)   {
            account.setMoney(value);
            return true;
        }
        return false;
    }
}
