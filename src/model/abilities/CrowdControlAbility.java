package model.abilities;
import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	
	private final Effect effect;
	
	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required, Effect effect) {
		super(name, cost, baseCoolDown, castRange, area, required);
		this.effect = effect;
	}
	
	
	
	
	public Effect getEffect() {
		return effect;
	}
	
	
	public String toString() {
		return super.toString() + "Effect: " + this.effect + "\n" + "\n";
	}




	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {

		for (Damageable d : targets) {

			Champion c = (Champion) d;
			Effect e = getEffect();
			Effect ec = (Effect) e.clone();
			
			c.getAppliedEffects().add(ec);
			e.apply(c);
			
			this.setCurrentCooldown(getBaseCooldown());
			
		}
		
	}
	
	
    
}
