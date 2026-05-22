package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.handler.Failed;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.mapper.OrderMapper;
import Grocify.GrocifyNear.model.*;
import Grocify.GrocifyNear.repository.*;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepo cartRepo;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShopWalletRepository shopWalletRepository;
    private final OrderMapper orderMapper;

    @Value("${razorpay.key}")
    private String KEY;

    @Value("${razorpay.secret}")
    private String SECRET;

    public OrderServiceImpl(CartRepo cartRepo,
                            OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            ShopWalletRepository shopWalletRepository,
                            OrderMapper orderMapper) {
        this.cartRepo = cartRepo;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shopWalletRepository = shopWalletRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public String getOrderDetails(Long userId) {
        try {
            Cart cart = cartRepo.findByUserUserId(userId)
                    .orElseThrow(() -> new ResourceNotAvailable("User not found"));

            double total = cart.getTotalPrice();

            RazorpayClient client = new RazorpayClient(KEY, SECRET);

            JSONObject object = new JSONObject();
            object.put("amount", (int) (total * 100)); // Razorpay expects paise as integer
            object.put("currency", "INR");
            object.put("receipt", "receipt_" + userId);

            com.razorpay.Order order = client.orders.create(object);
            return order.toString();

        } catch (RazorpayException e) {
            throw new Failed("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void placeOrder(Long userId, String razorpayPaymentId) {
        Cart cart = cartRepo.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotAvailable("User not found"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new Failed("Cart is empty");
        }


        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            if (product == null) {
                throw new Failed("A product in your cart no longer exists");
            }

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new Failed(product.getProductName() + " is out of stock");
            }

            double currentPrice = product.getCost();
            double cartPrice = cartItem.getSubtotal() / cartItem.getQuantity();

            if (currentPrice != cartPrice) {
                throw new Failed(product.getProductName() + " price has changed. Please review your cart");
            }
        }


        Grocify.GrocifyNear.model.Order order = orderMapper.orderFromCart(cart, razorpayPaymentId);

        orderRepository.save(order);

        // Create order items, update stock, update shop wallets
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = orderMapper.orderItemFromCartItem(cartItem,product,order);
            orderItemRepository.save(orderItem);

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            Shop shop = product.getShop();
            ShopWallet wallet = shopWalletRepository.findByShop(shop)
                    .orElseGet(() -> {
                        ShopWallet newWallet = new ShopWallet();
                        newWallet.setShop(shop);
                        newWallet.setPendingAmount(0);
                        newWallet.setSettledAmount(0);
                        return newWallet;
                    });

            wallet.setPendingAmount(wallet.getPendingAmount() + cartItem.getSubtotal());
            shopWalletRepository.save(wallet);
        }


        cart.getCartItems().clear();
        cart.setTotalPrice(0);

    }
}
