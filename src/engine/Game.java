package engine;

import exceptions.*;
import model.abilities.*;
import model.effects.*;
import model.world.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Game {

	private final Player firstPlayer;
	private final Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	
	private final Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private final PriorityQueue turnOrder;

	private static final int BOARDWIDTH = 5;
	private static final int BOARDHEIGHT = 5;


	public Game (Player firstPlayer, Player secondPlayer) {

		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		
		availableChampions = new ArrayList<>();
		availableAbilities = new ArrayList<>();
		
		
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		
		turnOrder = new PriorityQueue(6);
				
		placeChampions();
		placeCovers();
		
		prepareChampionTurns();

		
	}

	
	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Object[][] getBoard() {
		return board;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	
	
	private void placeChampions() {
		
		ArrayList<Champion> firstPlayerTeam = firstPlayer.getTeam();
		ArrayList<Champion> secondPlayerTeam = secondPlayer.getTeam();
		
		if (firstPlayerTeam.size() > 3 || secondPlayerTeam.size() > 3)
			return;
		
		
		
		for (int i = 0; i < firstPlayerTeam.size(); i++) {
			Champion currChamp = firstPlayerTeam.get(i);
			board[0][i + 1] = currChamp;
			currChamp.setLocation(new Point(0, i + 1));
		}
		
		for (int i = 0; i < secondPlayerTeam.size(); i++) {
			Champion currChamp = secondPlayerTeam.get(i);
			board[4][i + 1] = currChamp;
			currChamp.setLocation(new Point(4, i + 1));
		}

	}
	
	private void placeCovers() {
		
		int coverCount = 0;
		
		while (coverCount < 5){
		
		Random rand = new Random();
		
		int x = rand.nextInt(1, 4);
		int y = rand.nextInt(0, 5);
	
		if (board[x][y] == null) {
			board[x][y] = new Cover(x, y);
			coverCount++;

			}
		}
	}
	
	public static void loadAbilities(String filePath) throws IOException, IllegalStateException {
	
		
		if (availableAbilities.size() >= 45)
			return;
		
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		while (br.ready()) {
			
			
			if (availableAbilities.size() >= 45)
				return;
			
			String[] readAbilities = br.readLine().split(",");
			
			String type =  readAbilities[0];
			String name =  readAbilities[1];
			int manaCost = Integer.parseInt(readAbilities[2]);
			int castRange = Integer.parseInt(readAbilities[3]);
			int baseCoolDown = Integer.parseInt(readAbilities[4]);
			AreaOfEffect area = AreaOfEffect.valueOf(readAbilities[5]);
			int requiredActionsPerTurn = Integer.parseInt(readAbilities[6]);


			switch (type) {
				case "CC" -> {
					String effectName = readAbilities[7];
					int effectDuration = Integer.parseInt(readAbilities[8]);
					Effect effect;
					switch (effectName) {
						case "Disarm" -> effect = new Disarm(effectDuration);
						case "Dodge" -> effect = new Dodge(effectDuration);
						case "Embrace" -> effect = new Embrace(effectDuration);
						case "PowerUp" -> effect = new PowerUp(effectDuration);
						case "Root" -> effect = new Root(effectDuration);
						case "Shield" -> effect = new Shield(effectDuration);
						case "Shock" -> effect = new Shock(effectDuration);
						case "Silence" -> effect = new Silence(effectDuration);
						case "SpeedUp" -> effect = new SpeedUp(effectDuration);
						case "Stun" -> effect = new Stun(effectDuration);
						default -> throw new IllegalStateException("Unexpected value: " + effectName);
					}
					CrowdControlAbility CC = new CrowdControlAbility(name, manaCost, baseCoolDown, castRange, area, requiredActionsPerTurn, effect);
					availableAbilities.add(CC);
				}
				case "DMG" -> {
					int damageAmount = Integer.parseInt(readAbilities[7]);
					DamagingAbility DMG = new DamagingAbility(name, manaCost, baseCoolDown, castRange, area, requiredActionsPerTurn, damageAmount);
					availableAbilities.add(DMG);
				}
				case "HEL" -> {
					int healAmount = Integer.parseInt(readAbilities[7]);
					HealingAbility HEL = new HealingAbility(name, manaCost, baseCoolDown, castRange, area, requiredActionsPerTurn, healAmount);
					availableAbilities.add(HEL);
				}
				default -> throw new IllegalStateException("Unexpected value: " + type);
			}
		 }
	}
	 
	
	
	
	
	public static void loadChampions(String filePath) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		while (br.ready()) {
			
			
			if (availableChampions.size() >= 15)
				return;
			
			String[] readChampion = br.readLine().split(",");
			
			String type = readChampion[0];
			String name = readChampion[1];
			int maxHP =  Integer.parseInt(readChampion[2]);
			int mana = Integer.parseInt(readChampion[3]);
			int actions = Integer.parseInt(readChampion[4]);
			int speed = Integer.parseInt(readChampion[5]);
			int attackRange = Integer.parseInt(readChampion[6]);
			int attackDamage = Integer.parseInt(readChampion[7]);
			String ability1name = readChampion[8];
			String ability2name = readChampion[9];
			String ability3name = readChampion[10];


			switch (type) {
				case "H" -> {
					Hero H = new Hero(name, maxHP, mana, actions, speed, attackRange, attackDamage);
					addAbilityByName(ability1name, H.getAbilities());
					addAbilityByName(ability2name, H.getAbilities());
					addAbilityByName(ability3name, H.getAbilities());
					availableChampions.add(H);
				}
				case "V" -> {
					Villain V = new Villain(name, maxHP, mana, actions, speed, attackRange, attackDamage);
					addAbilityByName(ability1name, V.getAbilities());
					addAbilityByName(ability2name, V.getAbilities());
					addAbilityByName(ability3name, V.getAbilities());
					availableChampions.add(V);
				}
				case "A" -> {
					AntiHero A = new AntiHero(name, maxHP, mana, actions, speed, attackRange, attackDamage);
					addAbilityByName(ability1name, A.getAbilities());
					addAbilityByName(ability2name, A.getAbilities());
					addAbilityByName(ability3name, A.getAbilities());
					availableChampions.add(A);
				}
			}
		}
	}
   
	
	

	
	private static void addAbilityByName(String abilityName, ArrayList<Ability> abilityList) {

		for (Ability availableAbility : availableAbilities) {

			if (abilityList.size() >= 3)
				return;

			if (availableAbility.getName().equals(abilityName))     // Fetches ability by abilityName from availableAbilities
				abilityList.add(availableAbility);            	   //  and adds it to the provided abilityList of the Champion

		}
	}
	
	
	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}
	
	public Player checkGameOver() {
		
		if (firstPlayer.getTeam().size() == 0)
			return secondPlayer;
		else if (secondPlayer.getTeam().size() == 0)
			return firstPlayer;
		else
			return null;
		
	}
	
	private static boolean isValidPoint (Point p) {
		return p.x >= 0 && p.x <= 4 && p.y >= 0 && p.y <= 4;
	}
	
	
	private static int getDistance (Point a, Point b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	private Damageable getFirstDamageable(Point currPoint, Direction d) {

		ArrayList<Damageable> directionDamageables = getDirectionDamageables(currPoint, d);
		if (!directionDamageables.isEmpty())
			return directionDamageables.get(0);
		
		return null;
		
	}
	
		private ArrayList<Damageable> getDirectionDamageables (Point currPoint, Direction d) {
			
			Point p = new Point(currPoint.x, currPoint.y);
			ArrayList<Damageable> damageables = new ArrayList<>();
			
			switch (d) {
			
			case RIGHT:
				for (int i = p.y + 1; i <= 4; i++) {
					Damageable target = (Damageable) board[p.x][i];
					if (target != null)
						damageables.add(target);
				}
				break;
			case LEFT:
				for (int i = p.y - 1; i >= 0; i--) {
					Damageable target = (Damageable) board[p.x][i];
					if (target != null)
						damageables.add(target);
				}
				break;
			case UP:
				for (int i = p.x + 1; i <= 4; i++) {
					Damageable target = (Damageable) board[i][p.y];
					if (target != null)
						damageables.add(target);
				}
				break;
			case DOWN:
				for (int i = p.x - 1; i >= 0; i--) {
					Damageable target = (Damageable) board[i][p.y];
					if (target != null)
						damageables.add(target);
				}
				break;
			}
			
			return damageables;
		
	}
	
	public void move (Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		
		Champion currChamp = getCurrentChampion();
		Point currPoint = currChamp.getLocation();
		Point destination;
		int actionPoints = currChamp.getCurrentActionPoints();
		
		if (currChamp.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException("Champion cannot move while rooted");
		
		if (actionPoints < 1)
			throw new NotEnoughResourcesException("Insufficient action points");


		destination = switch (d) {
			case RIGHT -> new Point(currPoint.x, currPoint.y + 1);
			case LEFT -> new Point(currPoint.x, currPoint.y - 1);
			case UP -> new Point(currPoint.x + 1, currPoint.y);
			case DOWN -> new Point(currPoint.x - 1, currPoint.y);
		};
		

		if (!isValidPoint(destination))
			throw new UnallowedMovementException("Destination out of bounds");

		if (board[destination.x][destination.y] != null)
			throw new UnallowedMovementException("Unable to move to occupied cell");
			
		currChamp.setLocation(destination);
		board[destination.x][destination.y] = currChamp;
		board[currPoint.x][currPoint.y] = null;
		
		
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 1);
	}
	
	
	public void removeChampion(Champion c) {
		
		Point currPoint = c.getLocation();
		
		board[currPoint.x][currPoint.y] = null;
		c.setCondition(Condition.KNOCKEDOUT);
		firstPlayer.getTeam().remove(c);
		secondPlayer.getTeam().remove(c);
		
		Queue<Champion> aliveChamps = new LinkedList<>();
		
		while (!turnOrder.isEmpty()) {
			Champion champion = (Champion) turnOrder.remove();
			
			if (!champion.equals(c))
				aliveChamps.add(champion);

		}
		
		while (!aliveChamps.isEmpty()) 
			turnOrder.insert(aliveChamps.remove());
		
	}
	
	public void attack (Direction d) throws ChampionDisarmedException, NotEnoughResourcesException, CloneNotSupportedException {
		
		Champion currChamp = getCurrentChampion();
		Point currPoint = currChamp.getLocation();
		Damageable target = getFirstDamageable(currPoint, d);
	
		
		if (currChamp.hasEffect("Disarm"))
			throw new ChampionDisarmedException("Champion cannot attack while disarmed");
		
		if (currChamp.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException("Insufficient action points");
		
		if (target == null) {
			currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 2);
			return;
		}
		
		Point targetLocation = target.getLocation();
		
		
		int distance = getDistance(currPoint, targetLocation);
		
		if (distance > currChamp.getAttackRange())
			return;
		
		int	damage = currChamp.getAttackDamage();
		
		if (target instanceof Champion c) {

			if (c.hasEffect("Shield")) {
				c.removeEffectByName("Shield");
				currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 2);
				 return;
			}
			
			if (c.hasEffect("Dodge")) {
				double dodgeChance = Math.random();
				if (dodgeChance > 0.5) {
					currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 2);
					return;
					
				}
				
			}
			
		}

		
		if ((currChamp instanceof Villain && target instanceof Hero) || (currChamp instanceof Hero && target instanceof Villain))
			damage = (int) (1.5 * currChamp.getAttackDamage());
		else if ((currChamp instanceof AntiHero && !(target instanceof AntiHero)) || (!(currChamp instanceof AntiHero) && (target instanceof AntiHero)))
			damage = (int) (1.5 * currChamp.getAttackDamage());
		
		
		int hp = target.getCurrentHP() - damage;
		
		target.setCurrentHP(hp);
			
		if (target.getCurrentHP() <= 0 && target instanceof Champion) 
			removeChampion((Champion) target);
		else if (target instanceof Cover) 
			board[targetLocation.x][targetLocation.y] = null;
	
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 2);
		
		
	}
	
	
	private ArrayList<Point> getValidTargetPoints(Point p) {
		
		ArrayList<Point> validTargetPoints = new ArrayList<>();
		
		for (int i = p.x - 1; i <= p.x + 1; i++) {
			for (int j = p.y - 1; j <= p.y + 1; j++) {
				
				Point currPoint = new Point(i, j);
		
				if (isValidPoint(currPoint) && getDistance(currPoint, p) <= 2 && board[i][j] != null)
					validTargetPoints.add(currPoint);
			}
		}
		
		validTargetPoints.remove(p);
		return validTargetPoints;
		
	}
	
	
	private ArrayList<Champion> getTeam() {
		
	Champion currChamp = getCurrentChampion();
	boolean firstPlayerTeam = false;
		
		for (Champion c : firstPlayer.getTeam()) {
			if (c.equals(currChamp)) {
				firstPlayerTeam = true;
				break;
			}
		}
		
		if (firstPlayerTeam) 
			return firstPlayer.getTeam();
		else
			return secondPlayer.getTeam();
		
		}
	
	
	private ArrayList<Champion> getEnemyTeam() {
		
		Champion currChamp = getCurrentChampion();
		boolean isFirstPlayerTeam = false;
			
			for (Champion c : firstPlayer.getTeam()) {
				if (c.equals(currChamp)) {
					isFirstPlayerTeam = true;
					break;
				}
			}
			
			if (isFirstPlayerTeam)
				return secondPlayer.getTeam();
			else
				return firstPlayer.getTeam();
			
			}
	

	
	public void castAbility (Ability a) throws AbilityUseException, InvalidTargetException, NotEnoughResourcesException, CloneNotSupportedException  {
		
		if (a.getCurrentCooldown() > 0)
			throw new AbilityUseException("Ability is still on cooldown. Cooldown expires in " + a.getCurrentCooldown() + " turns");
		
		Champion currChamp = getCurrentChampion();
		
		if (currChamp.hasEffect("Silence"))
			throw new AbilityUseException("Champion cannot use abilities while silenced");
		
		Point currPoint = currChamp.getLocation();
		int manaCost = a.getManaCost();
		int pointCost = a.getRequiredActionPoints();
		int castRange = a.getCastRange();
		AreaOfEffect area = a.getCastArea();
		
		
		if (currChamp.getMana() < manaCost)
			throw new NotEnoughResourcesException("Insufficient mana. Action requires " + manaCost);

		if (currChamp.getCurrentActionPoints() < pointCost)
			throw new NotEnoughResourcesException("Insufficient action points.  Action requires " + pointCost);
		
		ArrayList<Champion> currChampTeam = getTeam();
		ArrayList<Champion> enemyTeam = getEnemyTeam();
		ArrayList<Damageable> targets = new ArrayList<>();
		
		switch(area) {
		
			case SELFTARGET:
				if (a instanceof HealingAbility) {
					targets.add(currChamp);
				} else if (a instanceof DamagingAbility) {
					throw new InvalidTargetException("Cannot cast damaging ability on yourself");
				} else if (a instanceof CrowdControlAbility CC) {
					Effect e = CC.getEffect();
					
					if (e.getType() == EffectType.DEBUFF)
						throw new InvalidTargetException("Cannot cast an ability that would apply a debuff on yourself");
					else if (e.getType() == EffectType.BUFF)
						targets.add(currChamp);
				}

				a.execute(targets);
				break;
			case TEAMTARGET:	
				if (a instanceof HealingAbility) {
					
					for (Champion c : currChampTeam) {
						if (getDistance(currPoint, c.getLocation()) <= castRange)
							targets.add(c);
					}
					
					a.execute(targets);
				} else if (a instanceof DamagingAbility) {
					for (Damageable d : enemyTeam) {
						if (getDistance(currPoint, d.getLocation()) <= castRange)
							targets.add(d);
					}
					
					a.execute(targets);
					for (Damageable c : targets) {
						if (c.getCurrentHP() <= 0)
							removeChampion((Champion) c);
					}
					
					
				} else if (a instanceof CrowdControlAbility CC) {
					Effect e = CC.getEffect();
					
					if (e.getType() == EffectType.DEBUFF) {
						for (Champion c : getEnemyTeam()) {
							if (getDistance(currPoint, c.getLocation()) <= castRange)
								targets.add(c);
						}
					}
					else if (e.getType() == EffectType.BUFF) {
						for (Champion c : getTeam()) {
							if (getDistance(currPoint, c.getLocation()) <= castRange)
								targets.add(c);
						}
					}
					
					CC.execute(targets);
					
				}
				break;
			case SURROUND:
				ArrayList<Point> ValidTargets = getValidTargetPoints(currPoint);
				
				if (a instanceof HealingAbility ha) {
					for (Point p : ValidTargets) {
						Damageable d = (Damageable) board[p.x][p.y];
						
						if (d instanceof Cover)
							continue;
						
						Champion c = (Champion) d;
						
						if (currChampTeam.contains(c))
							targets.add(c);
					}
					
					ha.execute(targets);
				}
				
				else if (a instanceof DamagingAbility da) {
					for (Point p : ValidTargets) {
						Damageable d = (Damageable) board[p.x][p.y];

						if (enemyTeam.contains(d) || d instanceof Cover)
							targets.add(d);
					}
					
					da.execute(targets);
					
					for (Damageable d : targets) {
						if (d.getCurrentHP() <= 0)
							removeChampion((Champion) d);
						
					}
				}
				
				else if (a instanceof CrowdControlAbility CC) {
					Effect e = CC.getEffect();
					
					for (Point p : ValidTargets) {
						Damageable d = (Damageable) board[p.x][p.y];
						
						if (d instanceof Cover)
							continue;
						
						Champion c = (Champion) d;
						
						if (e.getType() == EffectType.BUFF && currChampTeam.contains(c)) {
							targets.add(c);
							
						} else if (e.getType() == EffectType.DEBUFF && enemyTeam.contains(c)) {
							targets.add(c);
						}
	
						
					}
					
					CC.execute(targets);
					
					for (Damageable d : targets) {
						if (d.getCurrentHP() <= 0)
							removeChampion((Champion) d);
						
					}
				} else {
					throw new IllegalArgumentException("Unexpected ability type: " + a.getClass());
				}
				break;
			}
		
				currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - pointCost);
				currChamp.setMana(currChamp.getMana() - manaCost);
				a.setCurrentCooldown(a.getBaseCooldown());
		}
	
	
	public void castAbility(Ability a, Direction d) throws AbilityUseException, NotEnoughResourcesException, CloneNotSupportedException {

		if (a.getCurrentCooldown() > 0)
			throw new AbilityUseException("Ability is still on cooldown. Cooldown expires in " + a.getCurrentCooldown() + " turns");
		
		Champion currChamp = getCurrentChampion();
		
		if (currChamp.hasEffect("Silence"))
			throw new AbilityUseException("Champion cannot use abilities  while silenced");
		
		
		Point currPoint = currChamp.getLocation();
		ArrayList<Damageable> directionDamageables = getDirectionDamageables(currPoint, d);
		ArrayList<Damageable> targets = new ArrayList<>();
		
		int manaCost = a.getManaCost();
		int pointCost = a.getRequiredActionPoints();
		int castRange = a.getCastRange();


		if (currChamp.getMana() < manaCost)
			throw new NotEnoughResourcesException("Insufficient mana. Action requires " + manaCost);

		if (currChamp.getCurrentActionPoints() < pointCost)
			throw new NotEnoughResourcesException("Insufficient action points. Action requires " + pointCost);

		if (a instanceof HealingAbility ha) {
			for (Damageable da : directionDamageables) {
				if (getDistance(currPoint, da.getLocation()) <= castRange && getTeam().contains(da))
					targets.add(da);
			}
			
			ha.execute(targets);
			
		} else if (a instanceof DamagingAbility dm) {
			for (Damageable da : directionDamageables) {
				if (getDistance(currPoint, da.getLocation()) <= castRange && (getEnemyTeam().contains(da) || da instanceof Cover))
					targets.add(da);
			}
			
			dm.execute(targets);
			
		} else if (a instanceof CrowdControlAbility CC) {
			Effect e = CC.getEffect();
			
			if (e.getType() == EffectType.DEBUFF) {
				for (Damageable da : directionDamageables) {
					if (getDistance(currPoint, da.getLocation()) <= castRange && getEnemyTeam().contains(da))
						targets.add(da);
				}
			} else if (e.getType() == EffectType.BUFF) {
				for (Damageable da : directionDamageables) {
					if (getDistance(currPoint, da.getLocation()) <= castRange && getTeam().contains(da))
						targets.add(da);
			}
		}
			
			CC.execute(targets);
	}
		
		for (Damageable da : targets) {
			if (da.getCurrentHP() <= 0)
				removeChampion((Champion) da);
		}
		
		currChamp.setMana(currChamp.getMana() - manaCost);
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - pointCost);
		a.setCurrentCooldown(a.getBaseCooldown());
	}
	
	
	public void castAbility (Ability a, int x, int y) throws AbilityUseException, NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException {

		if (a.getCurrentCooldown() > 0)
			throw new AbilityUseException("Ability is still on cooldown. Cooldown expires in " + a.getCurrentCooldown() + " turns");
		
		Champion currChamp = getCurrentChampion();
		
		if (currChamp.hasEffect("Silence"))
			throw new AbilityUseException("Champion cannot use abilities while silenced");
		
		Point currPoint = currChamp.getLocation();
		Point targetLocation = new Point(x, y);
		
		if (!isValidPoint(targetLocation))
			throw new InvalidTargetException("Destination out of bounds");
		
		Damageable target = (Damageable) board[targetLocation.x][targetLocation.y];
		ArrayList<Damageable> targets = new ArrayList<>(1);

		
		int manaCost = a.getManaCost();
		int pointCost = a.getRequiredActionPoints();
		int distance = getDistance(currPoint, targetLocation);
		int abilityRange = a.getCastRange();


		if (currChamp.getMana() < manaCost)
			throw new NotEnoughResourcesException("Insufficient mana. Action requires " + manaCost);

		if (currChamp.getCurrentActionPoints() < pointCost)
			throw new NotEnoughResourcesException("Insufficient action points. Action requires " + pointCost);
			
		if (target == null)
			throw new InvalidTargetException("Cannot cast ability on an empty cell");
		
		if (distance > abilityRange)
			throw new AbilityUseException("Target out of range");

		ArrayList<Champion> currChampTeam = getTeam();
		ArrayList<Champion> enemyTeam = getEnemyTeam();

		
		if (a instanceof HealingAbility ha) {
			targets.add(target);

			if (target instanceof Cover)
				throw new InvalidTargetException("Cannot cast healing ability on a cover");
			
			if (currChampTeam.contains(target))
					ha.execute(targets);
			else
				throw new InvalidTargetException("Cannot cast healing ability on an emeny champion");
			
		} else if (a instanceof DamagingAbility da) {

			if (enemyTeam.contains(target) || target instanceof Cover)
				targets.add(target);
			else
				throw new InvalidTargetException("Cannot cast a damaging ability on friendly champion");
			
			da.execute(targets);
			
		} else if (a instanceof CrowdControlAbility CC) {
			Effect e = CC.getEffect();
			
			if (target instanceof Cover)
				throw new InvalidTargetException("Cannot cast crowd control ability on a cover");
			
			if (e.getType() == EffectType.DEBUFF) {
				if (enemyTeam.contains(target))
					targets.add(target);
				else
					throw new InvalidTargetException("Cannot cast an ability that would apply a debuff on a friendly champion");
			} else if (e.getType() == EffectType.BUFF) {
				if (currChampTeam.contains(target))
					targets.add(target);
				else
					throw new InvalidTargetException("Cannot cast an ability that would apply a buff on an enemy champion");
			}

			CC.execute(targets);
		}
		
		
		if (target.getCurrentHP() <= 0)
			removeChampion((Champion) target);
		
		currChamp.setMana(currChamp.getMana() - manaCost);
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - pointCost);
		a.setCurrentCooldown(a.getBaseCooldown());
		
	}
	

	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException, CloneNotSupportedException {
		Champion firstPlayerLeader = firstPlayer.getLeader();
		Champion secondPlayerLeader = secondPlayer.getLeader();
		Champion currChamp = this.getCurrentChampion();
		
		ArrayList<Champion> team = getTeam();
		ArrayList<Champion> enemyTeam = getEnemyTeam();
		
		Champion leader = null;
		
		if (!(currChamp.equals(firstPlayerLeader)) && !(currChamp.equals(secondPlayerLeader)))
			throw new LeaderNotCurrentException("The champion currently playing is not your leader");
		
		if ((currChamp.equals(firstPlayerLeader) && firstLeaderAbilityUsed) || (currChamp.equals(secondPlayerLeader) && secondLeaderAbilityUsed))
			throw new LeaderAbilityAlreadyUsedException("Leader ability already used");
		
		
		if (currChamp.equals(firstPlayerLeader))
			 leader = firstPlayerLeader;
		else if (currChamp.equals(secondPlayerLeader))
			 leader = secondPlayerLeader;
		
		
		
		if (leader instanceof Hero h) {
			h.useLeaderAbility(team);
			
		} else if (leader instanceof Villain v) {
			ArrayList<Champion> targets = new ArrayList<>();
			
			for (Champion c : enemyTeam) {
				if (c.getCurrentHP() < c.getMaxHP() * 0.3)
					targets.add(c);
			}
			
			v.useLeaderAbility(targets);
			
			for (Champion c : enemyTeam) {
				if (c.getCurrentHP() <= 0)
					removeChampion(c);
				}
			} else if (leader instanceof AntiHero ah) {
			ArrayList<Champion> allChampionsExceptLeaders = getAllChampions();
			
			allChampionsExceptLeaders.remove(firstPlayerLeader);
			allChampionsExceptLeaders.remove(secondPlayerLeader);

			ah.useLeaderAbility(allChampionsExceptLeaders);
		}

		if (firstPlayerLeader.equals(leader))
			firstLeaderAbilityUsed = true;
		else 
			secondLeaderAbilityUsed = true;
			
	}
	
	private ArrayList<Champion> getAllChampions() {
		
		ArrayList<Champion> allChampions = new ArrayList<>();

		allChampions.addAll(firstPlayer.getTeam());

		allChampions.addAll(secondPlayer.getTeam());
		
		return allChampions;
	}
	
	
	private void prepareChampionTurns() {
		ArrayList<Champion> allChampions = getAllChampions();
		
		for (Champion c : allChampions) {
			turnOrder.insert(c);
			
		}
	}
	
	
	public void endTurn() throws CloneNotSupportedException {
		
		ArrayList<Champion> champsToUpdate = new ArrayList<>();

		
			if (checkGameOver() != null) 
				return;
			
			if (!turnOrder.isEmpty())
				turnOrder.remove();
		
			if (turnOrder.isEmpty())
				prepareChampionTurns();
			
			Champion currChamp = getCurrentChampion();	
		
	
		while (currChamp.getCondition() == Condition.INACTIVE) {
			
			champsToUpdate.add(currChamp);
			
			turnOrder.remove();
			
			if (turnOrder.isEmpty())
				prepareChampionTurns();
			
			currChamp = getCurrentChampion();
		}
		
		champsToUpdate.add(currChamp);
		
		for (Champion c : champsToUpdate) {
			c.updateEffects();
			c.updateAbilities();
		}
		
		currChamp.setCurrentActionPoints(currChamp.getMaxActionPointsPerTurn());

	}
}
