package com.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.Model.BuyerOrder;
import com.Model.RetailOffer;
import com.Services.BuyerOrderService;
import com.Services.RetailOfferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {

    
    @Autowired
    private RetailOfferService civilOfferService;

    @Autowired
    private BuyerOrderService buyerOfferService;


    
    
    @GetMapping("/stats")
    public String getStatPage(Model model){
        
        

        List<Double> avgPrices = new ArrayList<Double>();

        String[] fruits = {"Cherry", "Peach", "Plums","Kiwi","Banana","Orange","Pear","Apple"};

        for (String fruit : fruits) {
            
            List<RetailOffer> fruitSpecific  = civilOfferService.getFiltered(fruit);

            Double avg = 0.0;

            for (RetailOffer offer : fruitSpecific) {
                avg = offer.getPrice_per_kg() + avg;
            }

            if (avg > 0.0) {
                avg = avg/ fruitSpecific.size();
            }

            avgPrices.add(avg);

        }

        model.addAttribute("avgArray", avgPrices);

        List<Double> avgVegPrices = new ArrayList<Double>();

        String[] vegetables = {"Tomatoes", "Lettuce", "Cabbage", "Broccoli", "Onions", "Carrots","Potatoes","Eggplants","Peppers","Corn","Zuccinis"};

        for (String vegetable : vegetables) {
            
            List<RetailOffer> vegSpecific  = civilOfferService.getFiltered(vegetable);

            Double vegavg = 0.0;

            for (RetailOffer offer : vegSpecific) {
                vegavg = offer.getPrice_per_kg() + vegavg;
            }

            if (vegavg > 0.0) {
                vegavg = vegavg/ vegSpecific.size();
            }

            avgVegPrices.add(vegavg);

        }

        model.addAttribute("avgVArray", avgVegPrices);


        /* WHOLESALE STATS */

        List<Double> avgWPrices = new ArrayList<Double>();

        //String[] fruits = {"Cherry", "Peach", "Plums","Kiwi","Banana","Orange","Pear","Apple"};

        for (String fruit : fruits) {
            
            List<BuyerOrder> fruitSpecific  = buyerOfferService.getFiltered(fruit);

            Double avg = 0.0;

            for (BuyerOrder offer : fruitSpecific) {
                avg = offer.getPrice_per_kg() + avg;
            }

            if (avg > 0.0) {
                avg = avg/ fruitSpecific.size();
            }

            avgWPrices.add(avg);

        }

        model.addAttribute("avgWArray", avgWPrices);

        List<Double> avgVegWPrices = new ArrayList<Double>();

        //String[] vegetables = {"Tomatoes", "Lettuce", "Cabbage", "Broccoli", "Onions", "Carrots","Potatoes","Eggplants","Peppers","Corn","Zuccinis"};

        for (String vegetable : vegetables) {
            
            List<BuyerOrder> vegSpecific  = buyerOfferService.getFiltered(vegetable);

            Double vegavg = 0.0;

            for (BuyerOrder offer : vegSpecific) {
                vegavg = offer.getPrice_per_kg() + vegavg;
            }

            if (vegavg > 0.0) {
                vegavg = vegavg/ vegSpecific.size();
            }

            avgVegWPrices.add(vegavg);

        }

        model.addAttribute("avgVWArray", avgVegWPrices);


        
        
        
        return "statsPage";
    }
    
}
