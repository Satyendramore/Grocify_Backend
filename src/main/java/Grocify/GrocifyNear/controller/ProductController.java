package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.dto.RequestDto.ProductRequestDto;
import Grocify.GrocifyNear.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
private final ProductService productService;
public ProductController(ProductService productService) {
    this.productService = productService;
}
@GetMapping
public ResponseEntity<?> findAllProducts() {
    return  new ResponseEntity<>(new ApiResponse<>("All product list",true,productService.getProducts()), HttpStatus.OK);
}

@PostMapping("/{shopId}")
    public ResponseEntity<?> addProductsByShopId(@PathVariable("shopId") Long shopId, @Valid @RequestBody ProductRequestDto productRequestDto) {
    return  new ResponseEntity<>(new ApiResponse<>(null,true,productService.addProduct(shopId,productRequestDto)), HttpStatus.ACCEPTED);
}

@PutMapping("/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable("id") Long id, @Valid @RequestBody ProductRequestDto productRequestDto) {
    return  new ResponseEntity<>(new ApiResponse<>(null,true,productService.updateProduct(id,productRequestDto)),HttpStatus.ACCEPTED);
}

@DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(new ApiResponse<>(null,true,productService.deleteProduct(id)),HttpStatus.OK);
}
}
