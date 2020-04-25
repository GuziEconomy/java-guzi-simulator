package guzi.world;

import core.AbstractWorld;
import core.Position;
import guzi.agent.HumanGuziUser;
import guzi.stat.GuziStats;

/**
 * Perfect environment for guzis !
 */
public class GuziWorld extends AbstractWorld<HumanGuziUser> {
  
  private GuziStats guziInCirculationGraph;

	public GuziWorld(final int hauteur, final int largeur) {
    super(hauteur, largeur);
    this.guziInCirculationGraph = new GuziStats();
  }
  
  @Override
  public void generateStats() {
  	this.guziInCirculationGraph.notifyNewDayPlayed(this);
  }
  
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(super.toString() + "\n");
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        final HumanGuziUser a = getAgent(new Position(i, j));
        if (a != null) {
          sb.append(a.getDetailledDescriptionState());
          sb.append("\n\n");
        }
      }
    }
    
    return sb.toString();
  }
}
