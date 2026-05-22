package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIRecommendationService {

    private final ProductRepo productRepository;

    private final AIService aiService;

    public AIRecommendationService(ProductRepo productRepository,AIService aiService) {
        this.productRepository = productRepository;
        this.aiService = aiService;
    }

    public String answerQuestion(String question) {



        List<Product> products = productRepository.findAll();

        StringBuilder context = new StringBuilder();

        for(Product product : products) {

            context.append(
                    "Product Name: "
                    + product.getProductName()
                    + "\n"
            );

            context.append(
                    "Price: "
                    + product.getCost()
                    + "\n"
            );

            context.append(
                    "Quantity: "
                    + product.getQuantity()
                    + "\n"
            );

            context.append(
                    "Shop: "
                    + product.getShop()
                    .getShopName()
                    + "\n\n"
            );
        }

        // final AI prompt

        String prompt = """

You are an intelligent grocery assistant.

User Question:
%s

Available Product Data:
%s

Answer intelligently.
Keep answer short and useful.

"""
                .formatted(
                        question,
                        context.toString()
                );



        return aiService.askAI(prompt);
    }
}