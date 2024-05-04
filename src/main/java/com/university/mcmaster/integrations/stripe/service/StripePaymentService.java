package com.university.mcmaster.integrations.stripe.service;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.response.MethodResponse;
import com.university.mcmaster.models.entities.User;

public class StripePaymentService {

//    public MethodResponse getConnectedAccount(User user, boolean createIfNotExists) {
//        AccountCreateParams params =
//                AccountCreateParams.builder()
//                        .setController(
//                                AccountCreateParams.Controller.builder()
//                                        .setLosses(
//                                                AccountCreateParams.Controller.Losses.builder()
//                                                        .setPayments(AccountCreateParams.Controller.Losses.Payments.APPLICATION)
//                                                        .build()
//                                        )
//                                        .setFees(
//                                                AccountCreateParams.Controller.Fees.builder()
//                                                        .setPayer(AccountCreateParams.Controller.Fees.Payer.APPLICATION)
//                                                        .build()
//                                        )
//                                        .setStripeDashboard(
//                                                AccountCreateParams.Controller.StripeDashboard.builder()
//                                                        .setType(AccountCreateParams.Controller.StripeDashboard.Type.EXPRESS)
//                                                        .build()
//                                        )
//                                        .build()
//                        )
//                        .build();
//
//        Account account = Account.create(params);
//    }
//
//    public MethodResponse getBankAccountLinkForUser(User user,String requestId) {
//        Account account = getConnectedAccount(user,false);
//        if(null == account) {
//            return MethodResponse.builder()
//                    .flag(true)
//                    .resultStr("no connected account found for user : " + user.getEmail())
//                    .build();
//        }
//    }
//
//    public MethodResponse createCheckoutSession(){
//        SessionCreateParams params =
//                SessionCreateParams.builder()
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .addLineItem(
//                                SessionCreateParams.LineItem.builder()
//                                        .setPrice("{{PRICE_ID}}")
//                                        .setQuantity(1L)
//                                        .build()
//                        )
//                        .setPaymentIntentData(
//                                SessionCreateParams.PaymentIntentData.builder()
//                                        .setApplicationFeeAmount(123L)
//                                        .setTransferData(
//                                                SessionCreateParams.PaymentIntentData.TransferData.builder()
//                                                        .setDestination("{{CONNECTED_ACCOUNT_ID}}")
//                                                        .build()
//                                        )
//                                        .build()
//                        )
//                        .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
//                        .setReturnUrl("https://example.com/checkout/return?session_id={CHECKOUT_SESSION_ID}")
//                        .build();
//
//        Session session = Session.create(params);
//    }

}
