package sts.saiyajin.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import sts.saiyajin.cards.types.ComboFinisher;
import sts.saiyajin.cards.utils.CardColors;
import sts.saiyajin.cards.utils.CardNames;
import sts.saiyajin.powers.ComboPower;
import sts.saiyajin.ui.CardPaths;

public class BackAttack extends ComboFinisher {

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CardNames.BACK_ATTACK);

	private static final int COST = 1;
	private static final int BASE_DAMAGE = 11;
	private static final int UPGRADE_DAMAGE = 4;
	private static final String ENEMY_NOT_VULN = "The enemy isn't vulnerable...";
	
	public BackAttack() {
		super(CardNames.BACK_ATTACK, cardStrings.NAME, CardPaths.BACK_ATTACK, COST, cardStrings.DESCRIPTION, 
		        AbstractCard.CardType.ATTACK,
		        CardColors.SAIYAN_CARD_COLOR,
		        AbstractCard.CardRarity.COMMON,
		        AbstractCard.CardTarget.ENEMY);
	    this.baseDamage = BASE_DAMAGE;
	    this.baseMagicNumber = 0;
	    this.magicNumber = 0;
	}
	
	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		boolean canUse = super.canUse(p, m);
		if (!canUse) return canUse;
		if (m != null && m.hasPower(VulnerablePower.POWER_ID)){
			return canUse;
		}
		cantUseMessage = ENEMY_NOT_VULN;
		return false;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
		}
	}

	@Override
	public void use(AbstractPlayer player, AbstractMonster monster) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(player, this.damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		if (magicNumber > 0) {
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, magicNumber));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, player, new WeakPower(monster, magicNumber, false), magicNumber));
		}
	}

	@Override
	public void updatedComboChain() {
		int diff = ComboPower.getCurrentPlayerComboCounter() - magicNumber;
		upgradeMagicNumber(diff);
	}

	@Override
	public void resetComboChain() {
		upgradeMagicNumber(-magicNumber);
	}

}
