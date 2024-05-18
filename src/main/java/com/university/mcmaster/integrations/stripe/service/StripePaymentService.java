package com.university.mcmaster.integrations.stripe.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.university.mcmaster.enums.Currency;
import com.university.mcmaster.models.dtos.response.MethodResponse;
import com.university.mcmaster.models.entities.StripeProduct;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.Utility;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StripePaymentService {

    private static final Logger log = LoggerFactory.getLogger(StripePaymentService.class);
    private static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey(EnvironmentVariables.STRIPE_API_KEY)
            .build();

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
            log.trace("creating listing product for payment");
            Product product = Product.create(productParams,requestOptions);
            PriceCreateParams params =
                    PriceCreateParams
                            .builder()
                            .setProduct(product.getId())
                            .setCurrency(unit.toString())
                            .setUnitAmount(amount)
                            .build();
            log.trace("creating price for product for payment");
            Price price = Price.create(params,requestOptions);
            res.setResult_1(StripeProduct.builder()
                    .stripeProductId(product.getId())
                    .stripePriceId(price.getId())
                    .createdOn(Instant.now().toEpochMilli())
                    .amount(price.getUnitAmount())
                    .build());
            log.trace("updating price in product");
            product.update(ProductUpdateParams.builder()
                            .setDefaultPrice(price.getId())
                    .build(),requestOptions);
        }catch (Exception e){
            e.printStackTrace();
            res.setFlag(true);
            res.setErrorMsg(e.getMessage());
        }
        return res;
    }

    public static MethodResponse<String,String,String> createCheckoutSession(
            String userEmail, String stripeProductId, Map<String,String> metaData, String successUrl,String failUrl
    ){
        MethodResponse<String,String,String> response = new MethodResponse<>();
        try {
            Product product = Product.retrieve(stripeProductId,requestOptions);
            String priceId = product.getDefaultPrice();
            Price price = Price.retrieve(priceId,requestOptions);
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                                    .putAllMetadata(metaData)
                                    .build())
                            .putAllMetadata(metaData)
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setCustomerEmail(userEmail)
                            .setCustomerCreation(SessionCreateParams.CustomerCreation.IF_REQUIRED)
                            .addAllPaymentMethodType(new ArrayList<>(){{
                                add(SessionCreateParams.PaymentMethodType.CARD);
//                                add(SessionCreateParams.PaymentMethodType.PAYNOW);
//                                add(SessionCreateParams.PaymentMethodType.PAYPAL);
                            }})
                            .setSuccessUrl(successUrl)
                            .setCancelUrl(failUrl)
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPrice(price.getId())
                                            .build())
                            .build();
            Session session = com.stripe.model.checkout.Session.create(params,requestOptions);
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
