package sts.saiyajin.cards.attacks;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.saiyajin.cards.types.ComboFinisher;
import sts.saiyajin.ui.CardPaths;
import sts.saiyajin.ui.vfx.SpiritSwordLaserEffect;
import sts.saiyajin.utils.BattleHelper;
import sts.saiyajin.utils.CardColors;
import sts.saiyajin.utils.CardNames;

public class SpiritSword extends ComboFinisher {

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CardNames.SPIRIT_SWORD);
	private static final int COST = 1;
	private static final int BASE_DAMAGE = 7; 
	private static final int UPGRADE_DAMAGE = 3; 
	private int BASE_KI_COST = 10;
	private int UPGRADED_KI_COST = -5;
	
	public SpiritSword() {
		super(CardNames.SPIRIT_SWORD, cardStrings.NAME, CardPaths.SPIRIT_SWORD, COST, cardStrings.DESCRIPTION, 
		        AbstractCard.CardType.ATTACK,
		        CardColors.SAIYAN_CARD_COLOR,
		        AbstractCard.CardRarity.UNCOMMON,
		        AbstractCard.CardTarget.ENEMY);
		
		this.baseDamage = BASE_DAMAGE;
		this.baseMagicNumber = BASE_KI_COST;
		this.magicNumber = this.baseMagicNumber;
		kiRequired = magicNumber;
	}

	@Override
	public void upgrade() {
		if (!upgraded){
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			upgradeMagicNumber(UPGRADED_KI_COST);
			upgradeKiRequired(UPGRADED_KI_COST);
		}
	}
	
	@Override
	public void finisher(AbstractPlayer player, AbstractCreature monster, int comboStacks) {
		int targetMonsterIdx = 0;
		ArrayList<AbstractMonster> orderedMonsters = BattleHelper.getCurrentBattleMonstersSortedOnX(false);
		for (AbstractMonster m : orderedMonsters) {
			if (m == monster) break;
			targetMonsterIdx++;
		}
		int finisherTargetIndex = targetMonsterIdx+1;
		while(finisherTargetIndex < orderedMonsters.size()) {
			if (orderedMonsters.get(finisherTargetIndex).isDeadOrEscaped()) {
				finisherTargetIndex++;
			} else {
				break;
			}
		}
		if (finisherTargetIndex < orderedMonsters.size()) {
			AbstractMonster mon2 = orderedMonsters.get(finisherTargetIndex);
			AbstractDungeon.actionManager.addToBottom(new VFXAction(player, new SpiritSwordLaserEffect(monster.hb.cX, monster.hb.cY, mon2.hb.cX, mon2.hb.cY), 0.1f));
			DamageInfo info = new DamageInfo(player, this.baseDamage * comboStacks);
			info.applyPowers(player, mon2);
			AbstractDungeon.actionManager.addToBottom(new DamageAction(mon2, info, AbstractGameAction.AttackEffect.NONE));
		}
	}
	
	@Override
	public void use(AbstractPlayer player, AbstractMonster monster) {
		AbstractDungeon.actionManager.addToBottom(new VFXAction(player, new SpiritSwordLaserEffect(player.hb.cX, player.hb.cY, monster.hb.cX, monster.hb.cY), 0.1f));
		AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(player, this.damage), AbstractGameAction.AttackEffect.NONE));
	}
}
