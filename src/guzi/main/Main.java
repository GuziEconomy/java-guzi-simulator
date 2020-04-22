package guzi.main;

import java.util.Random;

import core.MultiAgentSystem;
import core.exception.NoFreePositionException;
import guzi.agent.HumanGuziUser;
import guzi.world.GuziWorld;

public class Main {
  
  public static void main(final String[] args) {
    if (args.length < 5) {
      System.out.println("Usage : ");
      System.out.println("java -jar [latence] [nbDays] [gridWidth] [gridHeight] [nbGuziUsers]");
      System.out.println(" * latence : time in millisecond between two rounds");
      System.out.println(" * nbDays : number of days to play");
      System.out.println(" * gridWidth : number of case in a grid row");
      System.out.println(" * gridHeight : number of case in a grid column");
      System.out.println(" * nbGuziUsers : number of human guzi users ");
      return;
    }
    
    final int latence = Integer.parseInt(args[0]);
    final int nbDays = Integer.parseInt(args[1]);
    final int gridWidth = Integer.parseInt(args[2]);
    final int gridHeight = Integer.parseInt(args[3]);
    final int nbGuziUsers = Integer.parseInt(args[4]);
    
    final GuziWorld world = new GuziWorld(gridWidth, gridHeight);
    
    int cptNbGuziUsersCreated = 0;
    while (cptNbGuziUsersCreated++ < nbGuziUsers) {
      try {
        final Random rand = new Random();
        final Integer sellingIntentionProbability = rand.nextInt(100);
        final Integer buyingIntentionProbability = rand.nextInt(100);
        new HumanGuziUser(world, world.getRandomFreePosition(), sellingIntentionProbability, buyingIntentionProbability);
      }
      catch (final NoFreePositionException e) {
        // no more space in the grid, maximum number of users is reached
        break;
      }
    }
    
    final MultiAgentSystem simulator = new MultiAgentSystem(world);
    
    simulator.playRounds(nbDays);
  }
  
}
