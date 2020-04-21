package core;

import java.util.ArrayList;

/**
 * An agent is an entity capable of evolving in a multi-agent system. It belongs
 * to an coordinated world, in which it has a (variable) position. This position
 * determines its neighborhood including the agents with which it can interact.
 */
public abstract class AbstractAgent<T extends AbstractWorld<?>> {

    protected Position position;
    protected T world;
    protected Integer id;
    protected Integer previousDay;
    protected Integer nbRoundPlayed;

    public AbstractAgent(final T world, final Position pos) {
		this.position = pos;
		world.addAgent(this);
		this.world = world;
		this.nbRoundPlayed = 1;
		this.previousDay = 0;
    }

    /**
     * Agent's action during his round. He interacts with his close environment.
     */
    protected abstract void act();

    public abstract String getName();
    
    public abstract String getStrDisplayableOnGrid();
    
    /**
     * Agent's action during his round. He interacts with his close environment.
     */
    public void act(final int currentDay) {
		if (currentDay == this.previousDay) {
		    return;
		}
	
		this.previousDay = currentDay;
		act();
		this.nbRoundPlayed++;
    }

    public ArrayList<Position> getNeighbourPositions() {
    	return this.world.getNeighbourPositions(this.position);
    }

    protected ArrayList<Position> getOccupiedPositions(final ArrayList<Position> neighbours) {
		final int nbNeighbours = neighbours.size();
		final ArrayList<Position> occupiedPositions = new ArrayList<Position>();
		for (int i = 0; i < nbNeighbours; i++) {
		    final Position p = neighbours.get(i);
		    if (this.world.getAgent(neighbours.get(i)) != null) {
		    	occupiedPositions.add(p);
		    }
		}
	
		return occupiedPositions;
    }

    protected ArrayList<Position> getFreePositions(final ArrayList<Position> neighbours) {
		final int nbNeighbours = neighbours.size();
		final ArrayList<Position> freePositions = new ArrayList<Position>();
		for (int i = 0; i < nbNeighbours; i++) {
		    final Position p = neighbours.get(i);
	
		    if (this.world.getAgent(neighbours.get(i)) == null) {
		    	freePositions.add(p);
		    }
		}
	
		return freePositions;
    }

    public void goTo(final Position pos) {
		this.world.moveAgent(this.position, pos);
		this.setPosition(pos);
    }

    /**
     * Refresh the state of the agent at the end of a day.
     */
    public void refresh(final Integer currentDay) {
    	// default behaviour : doing nothing
    }

    @Override
    public String toString() {
		return "(" + this.position.getX() + "," + this.position.getY() + ") " + "[Agent " + this.id + "]" + " : "
			+ this.getName();
    }
    
    public int getId() {
    	return this.id;
    }

    public int getNbRoundPlayed() {
    	return this.nbRoundPlayed;
    }


    public Position getPosition() {
    	return this.position;
    }
    
    public void setId(final int id) {
    	this.id = id;
    }

    public void setPosition(final Position pos) {
    	this.position = pos;
    }

    public void setWorld(final T world) {
    	this.world = world;
    }

}
