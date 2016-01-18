package main;

import gui.RatBotsColorAssigner;
import brain.RandomRat;
import brain.fazerLicourice;
import util.PAGuiUtil;
import util.RatBotManager;
import world.RatBotWorld;

public class BotWorldRunner
{
    public static void main(String[] args)
    {
        PAGuiUtil.setLookAndFeelToOperatingSystemLookAndFeel();
        
        RatBotWorld world = new RatBotWorld();
        world.show();  //Just so that they see something while rats are loading.
        
        // Load RatBots from the 'ratbot' package
        RatBotManager.loadRatBotsFromClasspath("brain", world);
        RatBotsColorAssigner.resetCount();
        
        /*
         * This is another place where you can add RatBots to the match. 
         * Loading them here will save the time of selecting from the menu.  
         * 
         * world.add(new RandomRat());
         */
        world.add(new fazerLicourice()); 
        world.show();
    }
}
