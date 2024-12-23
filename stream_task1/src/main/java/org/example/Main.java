package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        Map<String, Double> totalByProduct = orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct, Collectors.summingDouble(Order::getCost)));
        List<Map.Entry<String, Double>> sortedProduct = totalByProduct.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .toList();
        List<Map.Entry<String, Double>> topProducts = sortedProduct.stream()
                .limit(3)
                .toList();
        double totalCostOfTop3 = topProducts.stream()
                .mapToDouble(Map.Entry::getValue)
                .sum();
        System.out.println("Total cost of top 3: " + totalCostOfTop3);
        System.out.println("Top 3 products: " + topProducts);
    }
}

class Order {
    private String product;
    private double cost;

    public Order(String product, double cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public double getCost() {
        return cost;
    }
}