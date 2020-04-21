package guzi.world;

import core.AbstractWorld;
import core.Position;
import guzi.agent.HumanGuziUser;

/**
 * Perfect environment for guzis !
 */
public class GuziWorld extends AbstractWorld<HumanGuziUser> {
  
  public GuziWorld(final int hauteur, final int largeur) {
    super(hauteur, largeur);
  }
  
  @Override
  public void generateStats() {
    // TODO Auto-generated method stub
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
