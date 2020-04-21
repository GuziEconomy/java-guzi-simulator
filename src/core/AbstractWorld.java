package core;

import java.util.ArrayList;
import java.util.Random;

import core.exception.AgentNullException;
import core.exception.InvalidPositionException;
import core.exception.NoFreePositionException;
import core.exception.AlreadOccupedPositionException;



/**
 * The world is the space of the multi-agent system.
 * It is a simplified space, defining only the agents existing there and their positions.
 */
public abstract class AbstractWorld<T extends AbstractAgent<?>> {
  
  protected int        		  cptID;
  protected T[][]             grid;
  protected int               height;
  protected int               width;
  protected int               nbAgents;
  protected ViewWorld view;
  
  @SuppressWarnings("unchecked")
  public AbstractWorld(final int height, final int width) {
    this.height = height;
    this.setWidth(width);
    this.grid = (T[][]) new AbstractAgent[width][height];
    this.nbAgents = 0;
    
    this.view = new ViewWorld(this);
    
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        this.grid[i][j] = null;
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  public void addAgent(final AbstractAgent<?> agent) throws InvalidPositionException, AlreadOccupedPositionException {
    final Position pos = agent.getPosition();
    if (!isValidPosition(pos)) {
      throw new InvalidPositionException();
    }
    
    if (this.grid[pos.getX()][pos.getY()] != null) {
      throw new AlreadOccupedPositionException();
    }
    
    agent.setId(this.cptID++);
    
    this.grid[pos.getX()][pos.getY()] = (T) agent;
    
    this.nbAgents++;
  }
  
  public abstract void generateStats();
  
  public T getAgent(final int x, final int y) throws InvalidPositionException {
    if (!isValidPosition(x, y)) {
      throw new InvalidPositionException();
    }
    return this.grid[x][y];
  }
  
  public T getAgent(final Position pos) {
    return getAgent(pos.getX(), pos.getY());
  }

  public ArrayList<Position> getNeighbourPositions(final Position pos) throws InvalidPositionException {
    if (!isValidPosition(pos)) {
      throw new InvalidPositionException();
    }
    
    final ArrayList<Position> voisins = new ArrayList<Position>();
    
    final int posX = pos.getX();
    final int posY = pos.getY();
    
    if (posY != 0) {
      voisins.add(new Position(posX, posY - 1));
      if (posX != 0) {
        voisins.add(new Position(posX - 1, posY - 1));
      }
      if (posX != this.width - 1) {
        voisins.add(new Position(posX + 1, posY - 1));
      }
    }
    
    if (posY != this.height - 1) {
      voisins.add(new Position(posX, posY + 1));
      if (posX != 0) {
        voisins.add(new Position(posX - 1, posY + 1));
      }
      if (posX != this.width - 1) {
        voisins.add(new Position(posX + 1, posY + 1));
      }
    }
    
    if (posX != 0) {
      voisins.add(new Position(posX - 1, posY));
    }
    
    if (posX != this.width - 1) {
      voisins.add(new Position(posX + 1, posY));
    }
    
    return voisins;
  }
  
  public Position getRandomFreePosition() throws NoFreePositionException {
    final Integer nbFreePositions = this.height * this.width - this.nbAgents;
    if (nbFreePositions > 0) {
      final Random rand = new Random();
      final int alea = rand.nextInt(nbFreePositions);
      int cpt = 0;
      
      for (int i = 0; i < this.width; i++) {
        for (int j = 0; j < this.height; j++) {
          if (this.grid[i][j] == null) {
            if (cpt == alea) {
              return new Position(i, j);
            }
            cpt++;
          }
        }
      }
    }
    
    throw new NoFreePositionException();
  }
  
  public T getRandomNeighbour(final Position position) {
    T firstAgent = null;
    final ArrayList<Position> neighbourPositions = getNeighbourPositions(position);
    if (!neighbourPositions.isEmpty()) {
      final Random rand = new Random();
      final int firstAleaPositionToCheck = rand.nextInt(neighbourPositions.size());
      // find agent from the alea position to end of the neighbour positions
      firstAgent = neighbourPositions.stream()
    		  .skip(firstAleaPositionToCheck)
    		  .filter(pos -> getAgent(pos) != null)
    		  .findFirst()
    		  .map(pos -> getAgent(pos))
    		  .orElse(null);
      if (firstAgent == null) {
        // if no one agent founded, search it from the beginning to the alea position
        firstAgent = neighbourPositions.stream()
        		.limit(firstAleaPositionToCheck)
        		.filter(pos -> getAgent(pos) != null)
        		.findFirst()
        		.map(pos -> getAgent(pos))
        		.orElse(null);
      }
    }
    return firstAgent;
  }

  public boolean isValidPosition(final int x, final int y) {
    return x < this.width && x >= 0 && y < this.height && y >= 0;
  }
  
  public boolean isValidPosition(final Position pos) {
    return isValidPosition(pos.getX(), pos.getY());
  }
  
  public void moveAgent(final Position posDep, final Position posArr) throws InvalidPositionException, AlreadOccupedPositionException {
    if (!isValidPosition(posArr) || !isValidPosition(posDep)) {
      throw new InvalidPositionException();
    }
    
    if (this.grid[posArr.getX()][posArr.getY()] != null) {
      throw new AlreadOccupedPositionException();
    }
    this.grid[posArr.getX()][posArr.getY()] = this.grid[posDep.getX()][posDep.getY()];
    this.grid[posDep.getX()][posDep.getY()] = null;
    
  }
  
  public void refreshView(final Integer currentDay) {
    this.view.refresh(currentDay);
  }
  
  public void removeAgent(final Position pos) throws InvalidPositionException {
    if (!isValidPosition(pos)) {
      throw new InvalidPositionException();
    }
    final T a = this.grid[pos.getX()][pos.getY()];
    if (a != null) {
      this.grid[pos.getX()][pos.getY()] = null;
      this.nbAgents--;
    }
    else {
      throw new AgentNullException();
    }
  }
  
  @Override
  public String toString() {
    String s = "";
    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        final T a = this.grid[i][j];
        if (a != null) {
          s += a.getStrDisplayableOnGrid();
        }
        else {
          s += " ";
        }
        s += " ";
      }
      s += "\n";
    }
    
    return s;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public int getNbAgents() {
    return this.nbAgents;
  }

  public int getWidth() {
    return this.width;
  }
  
  public void setHeight(final int height) {
    this.height = height;
  }
  
  public void setWidth(final int width) {
    this.width = width;
  }
}
