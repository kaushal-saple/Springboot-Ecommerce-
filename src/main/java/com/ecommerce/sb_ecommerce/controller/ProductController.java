package com.ecommerce.sb_ecommerce.controller;

import com.ecommerce.sb_ecommerce.Service.ProductService;
import com.ecommerce.sb_ecommerce.config.AppConstants;
import com.ecommerce.sb_ecommerce.payload.ProductDTO;
import com.ecommerce.sb_ecommerce.payload.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @Valid
            @RequestBody ProductDTO productDTO ,
            @PathVariable Long categoryId){

        ProductDTO savedProduct = productService.addProduct(productDTO,categoryId);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY,required = false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder

    ){
        ProductResponse products =  productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY,required = false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder
    ){
        ProductResponse productsByCategory =  productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY,required = false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder

    ){

         ProductResponse products =  productService.searchByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
         return new ResponseEntity<>(products, HttpStatus.FOUND);
    }

    @PutMapping("/public/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                         @RequestBody ProductDTO productDTO){
        ProductDTO updatedProduct = productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProduct =  productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
    }

    @PutMapping("/public/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam MultipartFile image) throws IOException {
        ProductDTO updatedImage = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedImage, HttpStatus.OK);

    }
}
