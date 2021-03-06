package sts.saiyajin.cards.attacks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
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
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;

import sts.saiyajin.cards.types.ComboFinisher;
import sts.saiyajin.powers.ComboPower;
import sts.saiyajin.ui.CardPaths;
import sts.saiyajin.utils.CardColors;
import sts.saiyajin.utils.CardNames;
import sts.saiyajin.utils.PowersHelper;

public class KameHameHa extends ComboFinisher {

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CardNames.KAME_HAME_HA);
	private static final int COST = 3;
	private static final int BASE_DAMAGE = 20; 
	private static final int UPGRADE_DAMAGE = 8; 
	private static final int KI_COST = 16; 
	private static final int UPGRADED_KI_COST = -6; 
	private final static String ONE_LINER = cardStrings.EXTENDED_DESCRIPTION[0];
	
	final Logger logger = LogManager.getLogger(KameHameHa.class);
	
	public KameHameHa() {
		super(CardNames.KAME_HAME_HA, cardStrings.NAME, CardPaths.KAME_HAME_HA, COST, cardStrings.DESCRIPTION, 
		        AbstractCard.CardType.ATTACK,
		        CardColors.SAIYAN_CARD_COLOR,
		        AbstractCard.CardRarity.COMMON,
		        AbstractCard.CardTarget.ENEMY);
		
		this.baseDamage = BASE_DAMAGE;
		this.baseMagicNumber = KI_COST;
		this.magicNumber = this.baseMagicNumber;
		this.kiRequired = magicNumber;
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
	public void use(AbstractPlayer player, AbstractMonster monster) {
		AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f, ONE_LINER, true));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(player, new MindblastEffect(player.dialogX, player.dialogY, player.flipHorizontal), 0.1f));
		DamageInfo damageInfo = new DamageInfo(player, this.damage, this.damageTypeForTurn);
		AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, damageInfo, AttackEffect.BLUNT_HEAVY));
	}

	@Override
	public void finisher(AbstractPlayer player, AbstractCreature monster, int comboStacks) {}
	
	@Override
	public void applyPowers() {
		int comboAmt = PowersHelper.getPlayerPowerAmount(ComboPower.POWER_ID);
		int costModify = COST - this.cost - comboAmt;
		if (-costModify > this.cost) costModify = -this.cost;
		this.modifyCostForCombat(costModify);
	}
	
	@Override
	public void onComboUpdated() {
		//I do +1 as this is triggered before the Combo is actually applied
		int comboAmt = PowersHelper.getPlayerPowerAmount(ComboPower.POWER_ID) + 1;
		int costModify = COST - this.cost - comboAmt;
		if (-costModify > this.cost) costModify = -this.cost;
		this.modifyCostForCombat(costModify);
		//Current cost is this.cost so the result of the #math is (COST - combo)
	}

	@Override
	public void onComboRemoved() {
		this.cost = COST;
		this.costForTurn = COST;
		this.isCostModified = false;
	}
}
