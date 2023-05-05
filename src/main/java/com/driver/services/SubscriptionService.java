package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);

        if(subscription.getSubscriptionType().equals("BASIC"))
        {
            subscription.setTotalAmountPaid(500+200*subscription.getNoOfScreensSubscribed());
        }
        else if(subscription.getSubscriptionType().equals("PRO"))
        {
            subscription.setTotalAmountPaid(800+250*subscription.getNoOfScreensSubscribed());
        }
        else
        {
            subscription.setTotalAmountPaid(1000+350*subscription.getNoOfScreensSubscribed());
        }
        //Save The subscription Object into the Db and return the total Amount that user has to pay
        user.setSubscription(subscription);
        userRepository.save(user);
        return subscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception
    {
        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();
        if(subscription.getSubscriptionType().equals(SubscriptionType.ELITE))
        {
            throw new Exception("Already the best Subscription");
        }
        int fare=subscription.getTotalAmountPaid();
        int newfare=0;
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC))
        {
            newfare=800+250*subscription.getNoOfScreensSubscribed();
            subscription.setSubscriptionType(SubscriptionType.PRO);
        }
        else
        {
            newfare=1000+350*subscription.getNoOfScreensSubscribed();
            subscription.setSubscriptionType(SubscriptionType.ELITE);
        }

        subscription.setTotalAmountPaid(newfare);

        user.setSubscription(subscription);

        userRepository.save(user);

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        return newfare-fare;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int rev=0;
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        for (Subscription subscription: subscriptions) {
            rev+=subscription.getTotalAmountPaid();
        }

        return rev;
    }

}
