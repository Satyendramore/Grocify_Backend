package Grocify.GrocifyNear.mapper;

import Grocify.GrocifyNear.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OrderMapper {

    public Order orderFromCart(Cart cart,String razorpayPaymentId){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalAmount(cart.getTotalPrice());
        order.setPaymentId(razorpayPaymentId);
        order.setPaymentStatus("SUCCESS");
        order.setOrderStatus("PLACED");
        order.setOrderItems(new ArrayList<>());
        return order;
    }

    public OrderItem orderItemFromCartItem(CartItem cartItem, Product product,Order order){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setShop(product.getShop());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPriceAtPurchase(product.getCost());
        orderItem.setSubtotal(cartItem.getSubtotal());
        return orderItem;
    }
}
