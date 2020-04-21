package core;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Multi agent system
 * Manage :
 * - world and is agents initialisations
 * - scheduling of agent actions for each round.
 */

public class MultiAgentSystem {
  
  public Integer          currentDay;
  protected AbstractWorld<?> world;
  private Integer          nbDayMax;
  /**
   * Time between two rounds (in millisecond)
   */
  private Integer          latence;

  public MultiAgentSystem(final int nbDayMax, final int latence) {
    this.currentDay = 1;
    this.nbDayMax = nbDayMax;
    this.latence = latence;
  }
  
  /**
   * Defines the stop condition.
   */
  public boolean isFinished() {
    return this.currentDay > this.nbDayMax;
  }
  
  /**
   * Play a round of the multi-agent system. Each agent is activated according to
   * random scheduling.
   */
  public void playRound() {
	// get the non null agents
    final AbstractAgent<?>[] agents = new AbstractAgent<?>[this.world.getNbAgents()];
    int cptNbAgents = 0;
    for (int i = 0; i < this.world.getWidth(); i++) {
      for (int j = 0; j < this.world.getHeight(); j++) {
        final AbstractAgent<?> a = this.world.getAgent(i, j);
        if (a != null) {
          agents[cptNbAgents++] = a;
        }
      }
    }
    
    // make them act
    while (cptNbAgents > 0) {
      final Random rand = new Random();
      final int alea = rand.nextInt(cptNbAgents);
      final AbstractAgent<?> agentAlea = agents[alea];
      if (agentAlea != null) {
        agentAlea.act(this.currentDay);
      }
      agents[alea] = agents[cptNbAgents - 1];
      agents[cptNbAgents - 1] = agentAlea;
      cptNbAgents--;
    }
    
    // then for each agent, refresh their states
    while (cptNbAgents < agents.length) {
      final AbstractAgent<?> a = agents[cptNbAgents++];
      if (a != null) {
        a.refresh(this.currentDay);
      }
    }
    
    this.world.generateStats();
    this.world.refreshView(this.currentDay);
    
    try {
      TimeUnit.MILLISECONDS.sleep(this.latence);
    }
    catch (final InterruptedException e) {
      // Handle exception
    }
  }
  
  /**
   * Run the multi agent system. It plays a new round for each day until the stop condition is reached.
   */
  public void run() {
    this.currentDay = 0;
    while (!isFinished()) {
      this.playRound();
      this.currentDay++;
    }
  }
  
  public AbstractWorld<?> getWorld() {
    return this.world;
  }
  
  public void setWorld(final AbstractWorld<?> env) {
    this.world = env;
  }
}
