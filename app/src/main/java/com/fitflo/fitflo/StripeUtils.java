package com.fitflo.fitflo;

/**
 * Created by cj on 2/4/16.
 */
import android.util.Log;

import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

public class StripeUtils {
    static String apiKey = "pk_test_6pRNASCoBOKtIshFeQd4XMUh";

    public static boolean validate(String cardNumber, int expMonth, int expYear, String cvc) {
        Card card = new Card(cardNumber,expMonth,expYear,cvc);
        return card.validateCard();
    }
    public static boolean getTokenAndSendToServer(String cardNumber, int expMonth, int expYear, String cvc) {
        Card card = new Card(cardNumber,expMonth,expYear,cvc);
        if(!card.validateCard()) {
            Log.d("stripe","card invalid");
            throw new RuntimeException("card invalid");
        }
        try {
            Stripe stripe = new Stripe(apiKey);
            stripe.createToken(card, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    error.printStackTrace();
                }

                @Override
                public void onSuccess(Token token) {
                    //send token to server
                    Log.d("stripe","successfuly retrieved token! Token is " + token.toString());
                }
            });
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static void main(String[] args) {

    }
}
