package guzi.money;

import java.util.NoSuchElementException;
import java.util.Queue;

import guzi.agent.HumanGuziUser;
import guzi.exception.ImpossiblePaymentException;
import guzi.exception.NoMoreGuzisException;

public class GuziOperationService {
  
  public static Integer addGuzasInPortfolio(final HumanGuziUser user, final Integer nbGuzasToAdd, final Integer currentDay) {
    return addGuzisInQueue(user.getPortfolio(), nbGuzasToAdd, currentDay);
  }

  public static Guzi removeGuzaFromPortfolio(final HumanGuziUser user) throws NoMoreGuzisException {
    return removeGuziFromQueue(user.getPortfolio());
  }
  
  public static Integer addGuzisInWallet(final HumanGuziUser user, final Integer nbGuzisToAdd, final Integer currentDay) {
    return addGuzisInQueue(user.getWallet(), nbGuzisToAdd, currentDay);
  }
  
  /**
   * @throws NoMoreGuzisException Exception thrown when there not enough guzis in wallet to substract the amount provided. In that case, the operation is
   *           totally canceled and the amount in wallet is unchanged
   */
  public static Integer removeFromWallet(final HumanGuziUser user, final Integer nbGuzisToRemove) throws NoMoreGuzisException {
    // check there is enough guzis in wallet
    final Integer nbGuzisInWallet = user.getWallet().size();
    if (nbGuzisToRemove > nbGuzisInWallet) {
      throw new NoMoreGuzisException();
    }
    
    // remove them
    int cptNbGuziRemoved = 0;
    while (cptNbGuziRemoved++ < nbGuzisToRemove) {
      removeGuziFromWallet(user);
    }
    
    return user.getWallet().size();
  }
  
  /**
   * Remove a unique Guzi from wallet
   */
  public static Guzi removeGuziFromWallet(final HumanGuziUser user) throws NoMoreGuzisException {
    return removeGuziFromQueue(user.getWallet());
  }
  
  public static Integer addInAccumulatedTotal(final HumanGuziUser user, final Integer nbGuzisToAdd) {
    final Integer newAccumulatedTotal = user.getAccumulatedTotal() + nbGuzisToAdd;
    user.setAccumulatedTotal(newAccumulatedTotal);
    return newAccumulatedTotal;
  }
  
  public static Integer addInBalance(final HumanGuziUser user, final Integer nbGuzisToAdd) {
	final Integer newBalance = user.getBalance() + nbGuzisToAdd;
    user.setBalance(newBalance);
    return newBalance;
  }
  
  public static Integer removeFromBalance(final HumanGuziUser user, final Integer nbGuzisToRemove) {
	final Integer newBalance = user.getBalance() - nbGuzisToRemove;
    user.setBalance(newBalance);
    return newBalance;
  }

  public static void pay(final HumanGuziUser payor, final HumanGuziUser recipient, final Integer price) throws ImpossiblePaymentException {
    try {
      removeFromWallet(payor, price);
    }
    catch (final NoMoreGuzisException e) {
      // the payor has not enough guzis in his wallet : cancel payment
      throw new ImpossiblePaymentException(e);
    }
    addInBalance(recipient, price);
  }
  
  public static void processDailyUserRefresh(final HumanGuziUser user, final Integer currentDay) {
    checkExpiredGuzis(user, currentDay);
    processDailyBalanceUpdate(user);
    processDailyIncome(user, currentDay);
  }
  
  /**
   * Check guzis in the wallet to put the expired ones in the accumulated total
   */
  public static void checkExpiredGuzis(final HumanGuziUser user, final Integer currentDay) {
    // check expired guzis
    while (isExpiredGuzi(user.getWallet().peek(), currentDay)) {
      try {
        removeGuziFromWallet(user);
        addInAccumulatedTotal(user, 1);
      }
      catch (final NoMoreGuzisException e) {
      }
    }
    
    // check expired guzas
    while (isExpiredGuzi(user.getPortfolio().peek(), currentDay)) {
      try {
        removeGuzaFromPortfolio(user);
        addInAccumulatedTotal(user, 1);
      }
      catch (final NoMoreGuzisException e) {
      }
    }
  }
  
  public static boolean isExpiredGuzi(final Guzi guzi, final Integer currentDay) {
    // a Guzi expires after 30 days
    return guzi != null && currentDay - guzi.getBirthday() > 30;
  }
  
  /**
   * Proceed to the update of balance value if it is positive by putting them in the accumulated total.
   */
  public static void processDailyBalanceUpdate(final HumanGuziUser user) {
    final Integer balance = user.getBalance();
    if (balance > 0) {
      addInAccumulatedTotal(user, balance);
      user.setBalance(0);
    }
  }
  
  public static void processDailyIncome(final HumanGuziUser user, final Integer currentDay) {
    final Integer dailyIncome = calculateDailyIncome(user);
    addGuzisInWallet(user, dailyIncome, currentDay);
    addGuzasInPortfolio(user, dailyIncome, currentDay);
  }
  
  public static Integer calculateDailyIncome(final HumanGuziUser user) {
    // The daily income is calculated from the accumulated total
    // The formula is : (cube root of user accumulated total) + 1
    return (int) (Math.cbrt(user.getAccumulatedTotal()) + 1);
  }
  
  private static Integer addGuzisInQueue(final Queue<Guzi> queue, final Integer nbGuzisToAdd, final Integer currentDay) {
    int cptNbGuzisAdded = 0;
    while (cptNbGuzisAdded++ < nbGuzisToAdd) {
      queue.add(new Guzi(currentDay));
    }
    return queue.size();
  }
  
  private static Guzi removeGuziFromQueue(final Queue<Guzi> queue) throws NoMoreGuzisException {
    // remove the next Guzi in the Queue (this is the oldest)
    try {
      return queue.remove();
    }
    catch (final NoSuchElementException e) {
      throw new NoMoreGuzisException(e);
    }
  }
}
