package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName())!=null)
        {
            throw new Exception("Series is already present");
        }
        int pId=webSeriesEntryDto.getProductionHouseId();
        ProductionHouse productionHouse=productionHouseRepository.findById(pId).get();
        WebSeries webSeries=new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setProductionHouse(productionHouse);
        productionHouse.getWebSeriesList().add(webSeries);
        double sum=0;
        int size=productionHouse.getWebSeriesList().size();
        for(WebSeries web:productionHouse.getWebSeriesList())
        {
            sum+=web.getRating();
        }
        productionHouse.setRatings(sum/size);

        productionHouseRepository.save(productionHouse);
        return webSeriesRepository.save(webSeries).getId();
        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo


    }

}
