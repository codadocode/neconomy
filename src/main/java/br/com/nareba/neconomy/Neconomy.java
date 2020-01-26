package br.com.nareba.neconomy;

import br.com.nareba.neconomy.account.Account;
import br.com.nareba.neconomy.account.AccountManager;
import br.com.nareba.neconomy.core.EventListener;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

public class Neconomy extends PluginBase {
    private AccountManager accountManager;
    private final String accountFileName = "accounts.json";
    private final Double initialValue = 1000.0;
    @Override
    public void onEnable() {
        getLogger().info(TextFormat.DARK_GREEN + "Economy system is initializing!");
        initializeFiles();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }
    @Override
    public void onDisable() {
        getLogger().info(TextFormat.DARK_GREEN + "Economy system has been disable!");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String strCommand = command.getName().toLowerCase();
        if (sender.isPlayer())   {
            Player player = (Player) sender;
            if (strCommand.equals("money"))   {
                if (player.isOp() || player.hasPermission("neconomy.money"))   {
                    switch(args.length)   {
                        case 0:
                            if (accountManager.accountExists(player.getName()))   {
                                player.sendMessage("Balance: " + accountManager.getMoneyType() + " " + accountManager.getAccountBalance(player.getName()));
                            }
                            break;
                        case 1:
                            if (player.isOp() || player.hasPermission("neconomy.money.others"))   {
                                String anotherPlayerName = args[0];
                                if (accountManager.accountExists(anotherPlayerName))   {
                                    player.sendMessage(anotherPlayerName + " Balance: " + accountManager.getMoneyType() + " " + accountManager.getAccountBalance(anotherPlayerName));
                                }else   {
                                    player.sendMessage(anotherPlayerName + " account doesn't exists.");
                                }
                            }
                            break;
                    }
                }
            }else if (strCommand.equals("givemoney"))   {
                if (player.isOp() || player.hasPermission("neconomy.givemoney"))   {
                    if (args.length > 0)   {
                        switch (args.length)   {
                            case 2:
                                String playerName = args[0];
                                Double value = new Double(args[1]);
                                if (accountManager.accountExists(playerName) && value > 0)   {
                                    accountManager.addValue(playerName, value);
                                    saveAccountsFile();
                                    getLogger().info(player.getName() + " has added " + accountManager.getMoneyType() + value.toString() + " to " + playerName + " account.");
                                    player.sendMessage("You has added " + accountManager.getMoneyType() + value.toString() + " to " + playerName + " account.");
                                }else   {
                                    player.sendMessage(playerName + " account doesn't exists or value to give is less than 0.");
                                }
                                break;
                        }
                    }
                }
            }else if (strCommand.equals("takemoney"))   {
                if (player.isOp() || player.hasPermission("neconomy.takemoney"))   {
                    if (args.length > 0)   {
                        switch (args.length)   {
                            case 2:
                                String playerName = args[0];
                                Double value = new Double(args[1]);
                                if (accountManager.accountExists(playerName) && value > 0)   {
                                    accountManager.subValue(playerName, value);
                                    saveAccountsFile();
                                    getLogger().info(player.getName() + " has taked " + accountManager.getMoneyType() + value.toString() + " to " + playerName + " account.");
                                    player.sendMessage("You has taked " + accountManager.getMoneyType() + value.toString() + " to " + playerName + " account.");
                                }else   {
                                    player.sendMessage(playerName + " account doesn't exists or value to take is less than 0.");
                                }
                                break;
                        }
                    }
                }
            }else if (strCommand.equals("pay"))   {
                if (player.isOp() || player.hasPermission("neconomy.pay"))   {
                    if (args.length > 0)   {
                        switch (args.length)   {
                            case 2:
                                String playerToPay = args[0];
                                Double value = new Double(args[1]);
                                if (accountManager.accountExists(player.getName()) && accountManager.accountExists(playerToPay) && value > 0)   {
                                    accountManager.pay(player.getName(), playerToPay, value);
                                    saveAccountsFile();
                                    getLogger().info(player.getName() + " has payed " + accountManager.getMoneyType() + value.toString() + " to " + playerToPay);
                                    player.sendMessage("You has payed " + accountManager.getMoneyType() + value.toString() + " to " + playerToPay);
                                }else   {
                                    player.sendMessage(playerToPay + " account doesn't exists or value to pay is than then 0.");
                                }
                                break;
                        }
                    }
                }
            }else if (strCommand.equals("setmoney"))   {
                if (player.isOp() || player.hasPermission("neconomy.setmoney"))   {
                    if (args.length > 0)   {
                        switch (args.length)   {
                            case 2:
                                String playerName = args[0];
                                Double value = new Double(args[1]);
                                if (accountManager.accountExists(playerName) && value > 0)   {
                                    accountManager.setAccountMoney(playerName, value);
                                    saveAccountsFile();
                                    getLogger().info(player.getName() + " has setted account from " + playerName + " to " + accountManager.getMoneyType() + value.toString() + ".");
                                    player.sendMessage("You has setted account from " + playerName + " to " + accountManager.getMoneyType() + value.toString() + ".");
                                }else   {
                                    player.sendMessage(playerName + " account doesn't exists or value to set is less than 0.");
                                }
                                break;
                        }
                    }
                }
            }
        }
        return true;
    }
    private void initializeFiles()   {
        getLogger().info("Loading files.");
        try   {
            getDataFolder().mkdir();
            File accountFile = new File(getDataFolder(), this.accountFileName);
            if (accountFile.exists())   {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String accountsJson = Utils.readFile(accountFile);
                Type mapAccountType = new TypeToken<Map<String, Account>>(){}.getType();
                Map<String, Account> tmpAccounts = gson.fromJson(accountsJson, mapAccountType);
                this.accountManager = new AccountManager(tmpAccounts);
            }else   {
                accountFile.createNewFile();
                this.accountManager = new AccountManager();
            }
            getLogger().info("Files has been loaded.");
        }catch (Exception e)   {
            e.printStackTrace();
        }
    }
    public AccountManager getAccountManager()   {
        return this.accountManager;
    }
    private void saveAccountsFile()   {
        try    {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String accountsJson = gson.toJson(accountManager.getAllAccounts());
            Utils.writeFile(new File(getDataFolder(), this.accountFileName), accountsJson);
        }catch (Exception e)   {
            e.printStackTrace();
        }
    }
}
