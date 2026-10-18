package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.LoginRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.UserRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.UserUpdateRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.NearbyShopResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.ProductResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.UserResponseDto;
import Grocify.GrocifyNear.enums.Role;
import Grocify.GrocifyNear.security.JwtService;
import Grocify.GrocifyNear.handler.Failed;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.mapper.ProductMapper;
import Grocify.GrocifyNear.mapper.UserMapper;
import Grocify.GrocifyNear.model.Cart;
import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.model.Shop;
import Grocify.GrocifyNear.model.User;
import Grocify.GrocifyNear.repository.CartRepo;
import Grocify.GrocifyNear.repository.ProductRepo;
import Grocify.GrocifyNear.repository.ShopRepo;
import Grocify.GrocifyNear.repository.UserRepo;
import Grocify.GrocifyNear.util.DistanceUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final DistanceUtil distanceUtil;
    private final ShopRepo shopRepo;
    private final UserMapper userMapper;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(DistanceUtil distanceUtil, ShopRepo shopRepo, UserMapper userMapper,
                           ProductRepo productRepo, ProductMapper productMapper,
                           UserRepo userRepo, CartRepo cartRepo,
                           PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.distanceUtil = distanceUtil;
        this.shopRepo = shopRepo;
        this.userMapper = userMapper;
        this.productRepo = productRepo;
        this.productMapper = productMapper;
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public List<NearbyShopResponseDto> getShopsByRadius(double lat, double lng, double radius) {
     try {
         List<Shop> shops = shopRepo.findAll();

         List<NearbyShopResponseDto> result = new ArrayList<>();
         for (Shop shop : shops) {
             double distance = distanceUtil.calculateDistance(lat, lng, shop.getLatitude(), shop.getLongitude());
             if (distance <= radius) {
                 NearbyShopResponseDto shopResponseDto = userMapper.nearbyShopResponseDtofromshop(shop, distance);
                 result.add(shopResponseDto);
             }
         }
        result.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
         return result;
     }
    catch (Exception e) {
         throw  new Failed("Failed to get shops because of this exception :" + e.getMessage());
    }

    }


    public List<ProductResponseDto> getProductsByName(String item) {
        List<Product> products= productRepo.findByProductName(item);
        List<ProductResponseDto> result = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDto productResponseDto= productMapper.productResponseDtoFromProduct(product);
            result.add(productResponseDto);
        }
        result.sort((o1, o2) -> Long.compare(o1.getCost(), o2.getCost()));
        return result;

    }

    @Override
    @Transactional
    public String addUser(UserRequestDto userRequestDto) {
        try {
            if (userRepo.existsByEmail(userRequestDto.getEmail())) {
                throw new Failed("User with this email already exists");
            }
            User newUser = userMapper.userFromUserRequestDto(new User(), userRequestDto);

            newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            newUser.setOrders(new ArrayList<>());
            User savedUser = userRepo.save(newUser);

            Cart cart = new Cart();
            cart.setUser(savedUser);
            cart.setTotalPrice(0);
            cart.setCartItems(new ArrayList<>());
            cartRepo.save(cart);

            return "User registered successfully";
        } catch (Failed e) {
            throw e;
        } catch (Exception e) {
            throw new Failed("Failed to create user: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String updateUser(UserUpdateRequestDto userRequestDto) {
        try {
            User existing = userRepo.findByEmail(userRequestDto.getEmail())
                    .orElseThrow(() -> new ResourceNotAvailable("User not found"));

            userMapper.userFromUserUpdateRequestDto(existing, userRequestDto);
            userRepo.save(existing);
            return "User info updated successfully";
        } catch (ResourceNotAvailable e) {
            throw e;
        } catch (Exception e) {
            throw new Failed("Failed to update user: " + e.getMessage());
        }
    }

    @Override
    public String login(LoginRequestDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotAvailable("User not found"));
        if(!user.getRole().equals(dto.getRoleName())) {
            throw new Failed("You are not allowed to login");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new Failed("Invalid password");
        }


        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    @Override
    public UserResponseDto getUser() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName();
       User user=userRepo.getUsersByEmail(email);
       if(user==null) {
           throw new ResourceNotAvailable("User not found");
       }
       UserResponseDto userResponseDto = userMapper.userResponseDtofromuser(user);
       return userResponseDto;
    }


}
