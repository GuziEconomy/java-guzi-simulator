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
  
  public Integer          currentRound;
  protected AbstractWorld<?> world;

  public MultiAgentSystem(AbstractWorld<?> world) {
    this.currentRound = 0;
    this.world = world;
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
        agentAlea.act(this.currentRound);
      }
      agents[alea] = agents[cptNbAgents - 1];
      agents[cptNbAgents - 1] = agentAlea;
      cptNbAgents--;
    }
    
    // then for each agent, refresh their states
    while (cptNbAgents < agents.length) {
      final AbstractAgent<?> a = agents[cptNbAgents++];
      if (a != null) {
        a.refresh(this.currentRound);
      }
    }
    
    this.world.generateStats();
    this.world.refreshView(this.currentRound);
    
    this.currentRound++;
  }
  
  public void playRounds(final int nbRoundToPlay) {
  	playRounds(nbRoundToPlay, 0);
  }
  
  /**
   * Play rounds of multi agent system. It plays a new round until the provided number of rounds is reached.
   */
  public void playRounds(final int nbRoundToPlay, final int latence) {
  	int nbRoundPlayed = 0;
    while (nbRoundPlayed++ < nbRoundToPlay) {
      this.playRound();
      
      try {
			  TimeUnit.MILLISECONDS.sleep(latence);
			}
			catch (final InterruptedException e) {
			}
    }
  }
  
  public AbstractWorld<?> getWorld() {
    return this.world;
  }
  
  public void setWorld(final AbstractWorld<?> env) {
    this.world = env;
  }
}
