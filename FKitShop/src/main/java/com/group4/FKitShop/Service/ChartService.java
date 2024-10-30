package com.group4.FKitShop.Service;

import com.group4.FKitShop.Entity.Product;
import com.group4.FKitShop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartService {
    @Autowired
    private ProductRepository productRepository;
    

    public Map<String,Object> getProductData() {
        List<Product> products = productRepository.findAll();
        String[] dataLabels = new String[products.size()];
        int[] dataValues = new int[products.size()];
        for (int i = 0; i < products.size(); i++) {
            dataLabels[i] = products.get(i).getName();
            dataValues[i] = products.get(i).getUnitOnOrder();
        }
        Map<String,Object> chartData = new HashMap<>();
        chartData.put("labels",dataLabels);
        chartData.put("data",dataValues);
        return chartData;
    }


}
