package guzi.agent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import core.AbstractAgent;
import core.AbstractWorld;
import core.Position;
import core.exception.NoFreePositionException;
import guzi.exception.ImpossiblePaymentException;
import guzi.money.Guzi;
import guzi.money.GuziOperationService;
import guzi.world.GuziWorld;

public class HumanGuziUser extends AbstractAgent<GuziWorld> {
  
  private static Random     rand = new Random();
  
  /**
   * Guzis in the user wallet.
   * We use a Queue to facilitate and optimize the ordered (by date) Guzi treatments.
   */
  private final Queue<Guzi> wallet;
  
  /**
   * Total of accumulated guzis in all user life
   */
  private Integer           accumulatedTotal;
  
  /**
   * Difference between recieved Guzis (Guzis + Guzas) and emit Guzas
   */
  private Integer           balance;
  
  /**
   * Guzas in the user portfolio.
   * We use a Queue to facilitate and optimize the ordered (by date) Guza treatments.
   */
  private final Queue<Guzi> portfolio;
  
  private Integer           sellingIntentionProbability;
  
  private Integer           buyingIntentionProbability;
  
  public HumanGuziUser(final GuziWorld env, final Position pos, final Integer sellingIntentionProbability, final Integer buyingIntentionProbability) {
    super(env, pos);
    this.wallet = new LinkedList<Guzi>();
    this.portfolio = new LinkedList<Guzi>();
    this.setAccumulatedTotal(0);
    this.setBalance(0);
    this.setBuyingIntentionProbability(buyingIntentionProbability);
    this.setSellingIntentionProbability(sellingIntentionProbability);
  }
  
  @Override
  protected void act() {
    // try to interact with someone !
    final HumanGuziUser randomNeighbour = this.world.getRandomNeighbour(this.getPosition());
    if (randomNeighbour != null) {
      if (HumanBehaviourService.hasBuyingIntention(this) && HumanBehaviourService.hasSellingIntention(randomNeighbour)) {
        final Integer price = rand.nextInt(wallet.size());
        try {
          GuziOperationService.pay(this, randomNeighbour, price);
        }
        catch (final ImpossiblePaymentException e) {
          e.printStackTrace();
        }
      }
    }
    else {
      // no neighbour, i can't interact with anybody, i can't live here anymore, i'm moving out
      try {
        this.goTo(this.world.getRandomFreePosition());
      }
      catch (final NoFreePositionException e) {
        // no neighboor and no free position ? 1*1 grid case ?
        // stay here
      }
    }
    
    // am i dying or giving birth ? let's try to give birth first
    if(HumanBehaviourService.isGivingBirth(this)) {
    	HumanBehaviourService.giveBirth(this);
    }
    
    if(HumanBehaviourService.isDying(this)) {
    	HumanBehaviourService.die(this);
    }
  }
  
  @Override
  public void refresh(final Integer currentDay) {
    GuziOperationService.processDailyUserRefresh(this, currentDay);
  }
  
  public String getDetailledDescriptionState() {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.getPosition());
    sb.append(" " + getName());
    sb.append(" [");
    sb.append("buyProb: ");
    sb.append(this.getBuyingIntentionProbability());
    sb.append(", ");
    sb.append("sellProb: ");
    sb.append(this.getSellingIntentionProbability());
    sb.append("] \n");
    sb.append("    wallet: " + getWallet().size() + "\n");
    sb.append("    balance: " + getBalance() + "\n");
    sb.append("    accumulated total: " + getAccumulatedTotal() + "\n");
    sb.append("    dailyIncome: " + GuziOperationService.calculateDailyIncome(this));
    
    return sb.toString();
  }
  
  @Override
  public String getName() {
    return "Human";
  }
  
  @Override
  public String getStrDisplayableOnGrid() {
    return GuziOperationService.calculateDailyIncome(this) + "";
  }
  
  public Integer getAccumulatedTotal() {
    return accumulatedTotal;
  }
  
  public Integer getBalance() {
    return balance;
  }
  
  public Integer getBuyingIntentionProbability() {
    return buyingIntentionProbability;
  }

  
  public Queue<Guzi> getPortfolio() {
    return portfolio;
  }
  
  public Integer getSellingIntentionProbability() {
    return sellingIntentionProbability;
  }
  
  public Queue<Guzi> getWallet() {
    return wallet;
  }
  
  public void setAccumulatedTotal(final Integer accumulatedTotal) {
    this.accumulatedTotal = accumulatedTotal;
  }
  
  public void setBalance(final Integer balance) {
    this.balance = balance;
  }
  
  public void setBuyingIntentionProbability(final Integer buyingIntentionProbability) {
    this.buyingIntentionProbability = buyingIntentionProbability;
  }
  
  public void setSellingIntentionProbability(final Integer sellingIntentionProbability) {
    this.sellingIntentionProbability = sellingIntentionProbability;
  }
}
