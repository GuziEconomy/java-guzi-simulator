package guzi.agent;

import java.util.Random;

import core.exception.NoFreePositionException;
import guzi.world.GuziWorld;

public class HumanBehaviourService {
	// there was 11,2 births for 1000 people in 2019 in France (source: Wikipedia)
	private static final Double DAILY_PROBABILITY_TO_GIVE_BIRTH = 0.0112/365;
	
	// there was 9.1 deaths for 1000 people in 2019 in France (source: Wikipedia)
	private static final Double DAILY_PROBABILITY_OF_DYING = 0.0091/365;
	
	public static final Random rand = new Random();

	
	
	public static boolean hasBuyingIntention(HumanGuziUser user) {
		return !user.getWallet().isEmpty() && rand.nextInt(100) < user.getBuyingIntentionProbability();
	}
	
	public static boolean hasSellingIntention(HumanGuziUser user) {
		return rand.nextInt(100) < user.getSellingIntentionProbability();
	}
	
	public static boolean isGivingBirth(HumanGuziUser user) {
		return rand.nextDouble() < DAILY_PROBABILITY_TO_GIVE_BIRTH;
	}
	
	public static void giveBirth(HumanGuziUser user) {
		try {
			new HumanGuziUser((GuziWorld) user.getWorld(), user.getWorld().getRandomFreePosition(), user.getSellingIntentionProbability(), user.getBuyingIntentionProbability());
		} catch (NoFreePositionException e) {
			// this world isn't big enough...
		}
	}
	
	public static boolean isDying(HumanGuziUser user) {
		return rand.nextDouble() < DAILY_PROBABILITY_OF_DYING;
	}
	
	public static void die(HumanGuziUser user) {
		user.getWorld().removeAgent(user.getPosition());
	}
}
