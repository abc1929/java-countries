package com.lambdaschool.countries.controllers;


import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class CountriesController {

    @Autowired
    CountryRepository countryrepos;

    @GetMapping(value = "/all", produces = {"application/json"})
    public ResponseEntity<?> GetAllCountriesRaw() {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    //    http://localhost:2019/names/all
    @GetMapping(value = "/names/all", produces = {"application/json"})
    public ResponseEntity<?> GetAllCountries() {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort(Comparator.comparing(Country::getName));
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    //    http://localhost:2019/names/start/u
    @GetMapping(value = "/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> GetCountriesU(@PathVariable char letter) {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        myList.sort(Comparator.comparing(Country::getName));

        return new ResponseEntity<>(myList.stream().filter(i -> Pattern.compile("^(?i)" + letter+ ".*").matcher(i.getName()).matches()), HttpStatus.OK);
    }

    //    http://localhost:2019/population/total
    @GetMapping(value = "/population/total", produces = {"application/json"})
    public ResponseEntity<?> GetPopulation() {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);

        return new ResponseEntity<>(myList.stream().map(i -> i.getPopulation()).reduce((a,b) -> a+b).get(), HttpStatus.OK);
    }

    //    http://localhost:2019/population/min
    @GetMapping(value = "/population/min", produces = {"application/json"})
    public ResponseEntity<?> GetPopulationMin() {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((a,b) -> (int) (a.getPopulation()-b.getPopulation()));

        return new ResponseEntity<>(myList.get(0), HttpStatus.OK);
    }

    //    http://localhost:2019/population/max
    @GetMapping(value = "/population/max", produces = {"application/json"})
    public ResponseEntity<?> GetPopulationMax() {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((a,b) -> (int) (b.getPopulation()-a.getPopulation()));

        return new ResponseEntity<>(myList.get(0), HttpStatus.OK);
    }


    //    http://localhost:2019/population/median
    // return the country with the median population
    @GetMapping(value = "/population/median", produces = {"application/json"})
    public ResponseEntity<?> GetPopulationMedian() {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((a,b) -> (int) (b.getPopulation()-a.getPopulation()));


        return new ResponseEntity<>(myList.get(myList.size()/2-1), HttpStatus.OK);
    }

    // http://localhost:2019/population/median/35
    // return the country with median population of some age
    @GetMapping(value = "/population/median/{age}", produces = {"application/json"})
    public ResponseEntity<?> GetCountriesWithMedianAge(@PathVariable int age) {
        List<Country> myList = new ArrayList<Country>();
        countryrepos.findAll().iterator().forEachRemaining(myList::add);
        return new ResponseEntity<>(myList.stream().filter(i-> i.getMedianage()==age).collect(Collectors.toList()), HttpStatus.OK);
    }
}
