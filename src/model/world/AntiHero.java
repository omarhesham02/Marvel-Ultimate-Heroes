package model.world;

import java.util.ArrayList;

import model.effects.Stun;

public class AntiHero extends Champion{

	public AntiHero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) throws Exception {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
		
	}

	public void useLeaderAbility(ArrayList<Champion> targets) {
		
		for (Champion c : targets) {
			Stun s = new Stun(2);
			c.getAppliedEffects().add(s);
			s.apply(c);
		}

	}	
	
}
