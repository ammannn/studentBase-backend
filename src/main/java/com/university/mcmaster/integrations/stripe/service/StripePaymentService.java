package com.university.mcmaster.integrations.stripe.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.university.mcmaster.enums.Currency;
import com.university.mcmaster.models.dtos.response.MethodResponse;
import com.university.mcmaster.models.entities.StripeProduct;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.Utility;
import org.checkerframework.checker.units.qual.A;

import java.time.Instant;
import java.util.ArrayList;

public class StripePaymentService {

    public static MethodResponse<StripeProduct,?,?> createProductAndPrice(
            String productName, String productDescription, Currency unit, long amount
    ){
        MethodResponse<StripeProduct,?,?> res = new MethodResponse<>();
        try {
            ProductCreateParams productParams =
                    ProductCreateParams.builder()
                            .setName(productName)
                            .setDescription(productDescription)
                            .build();
            Product product = Product.create(productParams,RequestOptions.builder()
                    .setApiKey(EnvironmentVariables.STRIPE_API_KEY).build());
            PriceCreateParams params =
                    PriceCreateParams
                            .builder()
                            .setProduct(product.getId())
                            .setCurrency(unit.toString())
                            .setUnitAmount(amount)
                            .build();
            Price price = Price.create(params, RequestOptions.builder()
                    .setApiKey(EnvironmentVariables.STRIPE_API_KEY).build());
            res.setResult_1(StripeProduct.builder()
                    .stripeProductId(product.getId())
                    .stripePriceId(price.getId())
                    .createdOn(Instant.now().toEpochMilli())
                    .amount(price.getUnitAmount())
                    .build());
        }catch (Exception e){
            e.printStackTrace();
            res.setFlag(true);
            res.setErrorMsg(e.getMessage());
        }
        return res;
    }

    public static MethodResponse<String,String,String> createCheckoutSession(String userEmail, String stripeProductId){
        MethodResponse<String,String,String> response = new MethodResponse<>();
        try {
            Product product = Product.retrieve(stripeProductId);
            Price price = product.getDefaultPriceObject();
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setCustomerEmail(userEmail)
                            .setCustomerCreation(SessionCreateParams.CustomerCreation.IF_REQUIRED)
                            .addAllPaymentMethodType(new ArrayList<>(){{
                                add(SessionCreateParams.PaymentMethodType.CARD);
                                add(SessionCreateParams.PaymentMethodType.PAYNOW);
                                add(SessionCreateParams.PaymentMethodType.PAYPAL);
                            }})
                            .setSuccessUrl(Utility.getPlatformUrl() + "/payment-success")
                            .setCancelUrl(Utility.getPlatformUrl() + "/payment-canceled")
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPrice(price.getId())
                                            .build())
                            .build();
            Session session = com.stripe.model.checkout.Session.create(params,RequestOptions.builder()
                    .setApiKey(EnvironmentVariables.STRIPE_API_KEY).build());
            response.setResult_1(session.getUrl());
            response.setResult_2(session.getId());
            response.setResult_3(session.getPaymentIntent());
        }catch (Exception e){
            e.printStackTrace();
            response.setFlag(true);
            response.setErrorMsg(e.getMessage());
        }
        return response;
    }

}
