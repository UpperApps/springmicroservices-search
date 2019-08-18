package com.brownfield.pss.search.controller;

import com.brownfield.pss.search.component.SearchComponent;
import com.brownfield.pss.search.entity.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RefreshScope
@RestController
@RequestMapping("/search")
class SearchRestController {

    private SearchComponent searchComponent;
    @Value("${originairports.shutdown}")
    private String originAirportShutdownList;
    private final Logger logger = LoggerFactory.getLogger(SearchRestController.class);

    @Autowired
    public SearchRestController(SearchComponent searchComponent){
        this.searchComponent = searchComponent;
    }

    @RequestMapping(value="/get", method = RequestMethod.POST)
    List<Flight> search(@RequestBody SearchQuery query){
        logger.info("Input : " + query.toString());
        if (Arrays.asList(originAirportShutdownList.split(",")).contains(query.getOrigin())) {
            logger.info("The origin airport is in shutdown state");
            return new ArrayList<Flight>();
        }
        return searchComponent.search(query);
    }

}
