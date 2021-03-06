package sts.saiyajin.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.saiyajin.cards.special.KiBurn;
import sts.saiyajin.cards.tags.SaiyajinCustomCardTags;
import sts.saiyajin.cards.types.SaiyanCard;
import sts.saiyajin.ui.CardPaths;
import sts.saiyajin.ui.vfx.FireEffect;
import sts.saiyajin.utils.CardColors;
import sts.saiyajin.utils.CardNames;
import sts.saiyajin.utils.PowersHelper;

public class DragonFist extends SaiyanCard {

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CardNames.DRAGON_FIST);

	private static final int COST = 1;
	private static final int BASE_DAMAGE = 12;
	private static final int UPGRADE_DAMAGE = 4;

	private static final int FIRE_FX_TIMES = 4;
	
	public DragonFist() {
		super(CardNames.DRAGON_FIST, cardStrings.NAME, CardPaths.DRAGON_FIST, COST, cardStrings.DESCRIPTION, 
		        AbstractCard.CardType.ATTACK,
		        CardColors.SAIYAN_CARD_COLOR,
		        AbstractCard.CardRarity.COMMON,
		        AbstractCard.CardTarget.ENEMY);
	    this.baseDamage = BASE_DAMAGE;
	    this.tags.add(SaiyajinCustomCardTags.COMBO_FOLLOW_UP);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_DAMAGE);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public void use(AbstractPlayer player, AbstractMonster monster) {
        if (!this.upgraded) {
        	AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new KiBurn(), 1));
        }
        for (int i = 0 ; i < FIRE_FX_TIMES; i++) {
        	AbstractDungeon.actionManager.addToBottom(new VFXAction(new FireEffect(player.hb.cX, player.hb.cY, monster.hb.cX, monster.hb.cY, 0.125f), 0.125f));
        }
        AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(player, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToTop(new ExhaustAction(player, player, 1, false));
		PowersHelper.comboFollowUp();
	}

}
