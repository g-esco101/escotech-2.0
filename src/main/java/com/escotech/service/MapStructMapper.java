package com.escotech.service;

import com.escotech.dto.*;
import com.escotech.entity.Image;
import com.escotech.entity.Order;
import com.escotech.form.*;
import com.escotech.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MapStructMapper {

//    @Mappings({
//            @Mapping(target="id", ignore = true),
//            @Mapping(target="productId", source="product.id"),
//            @Mapping(target="quantity", ignore = true),
//            @Mapping(target="lineTotal", ignore = true)
//    })
//    OrderLine productToOrderLine(Product product);

    void purchaserFormToOrder(PurchaserForm purchaserForm, @MappingTarget Order order);

    void addressFormToOrder(AddressForm addressForm, @MappingTarget Order order);

    void productFormToProduct(ProductForm productForm, @MappingTarget Product product);

    Product productFormToPoduct(ProductForm productForm);

    @Mappings({
            @Mapping(target="productId", source="product.id")
    })
    CartProduct productToCartProduct(Product product);

    Order cartFormToOrder(Cart cart);

    OrdersView orderToOrdersView(Order order);

    OrderView orderToOrderView(Order order);

    default ProductForm productToProductForm(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductForm productForm = new ProductForm();

        productForm.setId( product.getId() );
        productForm.setCategory( product.getCategory() );
        productForm.setBrand( product.getBrand() );
        productForm.setModel( product.getModel() );
        productForm.setSerialNumber( product.getSerialNumber() );
        productForm.setDescription( product.getDescription() );
        productForm.setInventoryCount( product.getInventoryCount() );
        productForm.setUnitCost( product.getUnitCost() );
        productForm.setUnitShipping( product.getUnitShipping() );
        productForm.setCondition( product.getCondition() );
        List<Image> images = product.getImages();
        long[] imageIds;
        if (images != null) {
            imageIds = product.getImages().stream()
                    .mapToLong(Image::getId)
                    .toArray();
        } else {
            imageIds = new long[0];
        }
        productForm.setImageIds(imageIds);
        return productForm;
    }

    default ProductView productToProductView(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductView productView = new ProductView();

        productView.setId( product.getId() );
        productView.setCategory( product.getCategory() );
        productView.setBrand( product.getBrand() );
        productView.setModel( product.getModel() );
        productView.setSerialNumber( product.getSerialNumber() );
        productView.setDescription( product.getDescription() );
        productView.setInventoryCount( product.getInventoryCount() );
        productView.setUnitCost( product.getUnitCost() );
        productView.setUnitShipping( product.getUnitShipping() );
        productView.setCondition( product.getCondition() );
        List<Image> images = product.getImages();
        if (images != null) {
            productView.setImageCount( product.getImages().size() );
        } else {
            productView.setImageCount( 0 );
        }

        return productView;
    }
}
