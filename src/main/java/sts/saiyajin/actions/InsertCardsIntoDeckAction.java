package sts.saiyajin.actions;

import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InsertCardsIntoDeckAction extends AbstractGameAction {
    
	final Logger logger = LogManager.getLogger(InsertCardsIntoDeckAction.class);
	CardGroup toInsert;
	Predicate<AbstractCard> predicate = null;
	
	public InsertCardsIntoDeckAction(CardGroup toInsert) {
		this(toInsert, null);
	}
	
    public InsertCardsIntoDeckAction(CardGroup toInsert, Predicate<AbstractCard> pred) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.toInsert = toInsert;
        this.predicate = pred;
        this.duration = Settings.ACTION_DUR_MED;
    }
    
    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
        	while (toInsert.size() > 0){
        		AbstractCard c = toInsert.getTopCard();
        		toInsert.removeCard(c);
        		if (predicate != null && !predicate.test(c)) continue;
        		toInsert.moveToDeck(c, true);
        	}
        }
        this.tickDuration();
    }
}
