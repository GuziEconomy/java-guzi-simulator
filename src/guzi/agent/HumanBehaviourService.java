package guzi.agent;

import java.util.Random;

public class HumanBehaviourService {
	public static final Random rand = new Random();
	
	public static boolean hasBuyingIntention(HumanGuziUser user) {
		return !user.getWallet().isEmpty() && rand.nextInt(100) < user.getBuyingIntentionProbability();
	}
	
	public static boolean hasSellingIntention(HumanGuziUser user) {
		return rand.nextInt(100) < user.getSellingIntentionProbability();
	}
	
}
