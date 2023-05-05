package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        return userRepository.save(user).getId();

    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        User user = userRepository.findById(userId).get();
        int value=0;
        if (user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC))
            value=1;
        else if (user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO))
            value=2;
        else
            value=3;
        List<WebSeries> webSeriesList=webSeriesRepository.findAll();
        int cnt=0;
        for (WebSeries webSeries: webSeriesList) {
            int webval=0;
            if (webSeries.getSubscriptionType().equals(SubscriptionType.BASIC))
                webval=1;
            else if (webSeries.getSubscriptionType().equals(SubscriptionType.PRO))
                webval=2;
            else
                webval=3;

            if(webSeries.getAgeLimit()<=user.getAge() && webval<=value)
            {
                cnt++;
            }
        }
        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository


        return cnt;
    }


}
