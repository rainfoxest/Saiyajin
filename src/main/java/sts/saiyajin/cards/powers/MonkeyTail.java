package sts.saiyajin.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import sts.saiyajin.actions.InsertCardsIntoDeckAction;
import sts.saiyajin.cards.skills.FullMoon;
import sts.saiyajin.cards.types.SaiyanCard;
import sts.saiyajin.ui.CardPaths;
import sts.saiyajin.utils.CardColors;
import sts.saiyajin.utils.CardNames;

public class MonkeyTail extends SaiyanCard
{
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CardNames.MONKEY_TAIL);
	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;
	private static final int BASE_STR = 1;
	private static final int UPGRADED_STR = 1;
	
    
    public MonkeyTail() {
		super(CardNames.MONKEY_TAIL, cardStrings.NAME, CardPaths.MONKEY_TAIL, COST, cardStrings.DESCRIPTION, 
		        AbstractCard.CardType.POWER,
		        CardColors.SAIYAN_CARD_COLOR,
		        AbstractCard.CardRarity.RARE,
		        AbstractCard.CardTarget.SELF);
		this.baseMagicNumber = BASE_STR;
		this.magicNumber = this.baseMagicNumber;
    }
    
    @Override
    public void use(final AbstractPlayer player, final AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, this.magicNumber), this.magicNumber));
		CardGroup cardsToAdd = new CardGroup(CardGroupType.UNSPECIFIED);
		cardsToAdd.addToBottom(new FullMoon().makeCopy());
		cardsToAdd.addToBottom(new GreatApeForm().makeCopy());
		AbstractDungeon.actionManager.addToBottom(new InsertCardsIntoDeckAction(cardsToAdd));
		
    }
    
    @Override
    public AbstractCard makeCopy() {
        return new MonkeyTail();
    }
    
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_STR);
            upgradeBaseCost(UPGRADED_COST);
        }
    }
}