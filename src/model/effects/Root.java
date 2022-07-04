package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root (int duration) {
		super("Root", duration, EffectType.DEBUFF);
	}
	
	
	public void apply (Champion c) {

		
		if (c.getCondition() == Condition.KNOCKEDOUT || c.getCondition() == Condition.INACTIVE)
			return;
	
		c.setCondition(Condition.ROOTED);

		
	}

	
	public void remove (Champion c) {
		
		
		if ( !c.hasEffect("Root") && c.getCondition() == Condition.ROOTED)
				c.setCondition(Condition.ROOTED);
		else
			c.setCondition(c.getCondition());
	}
	
}
