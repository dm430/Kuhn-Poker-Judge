package AI;

import AI.Card;
import AI.Move;

/**
 * Created by devin on 11/2/15.
 */
public abstract class PlayerAI {
    private Card myCard;

    public Card showHand() {
        return myCard;
    }

    public void setCard(Card card) {
        this.myCard = card;
    }

    public abstract Move play(Move opponentsMove);

    public abstract Move play();
}
