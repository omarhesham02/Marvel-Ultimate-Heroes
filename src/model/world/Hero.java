package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

	public Hero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) throws Exception {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
		
	}


	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException  {
		for (Champion c : targets) {
			
			ArrayList<Effect> effects = c.getAppliedEffects();
			ArrayList<Effect> effectsToRemove = new ArrayList<>();
			
			for (Effect e : effects) {
				
				if (e.getType() == EffectType.DEBUFF)
					effectsToRemove.add(e);
				
			}
			
			for (Effect e : effectsToRemove) {
				effects.remove(e);
			}
			
			Embrace em = new Embrace(2);
			c.getAppliedEffects().add(em);
			em.apply(c);
			
		}
		
	}
	
	

}
