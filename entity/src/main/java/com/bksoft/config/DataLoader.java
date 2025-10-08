package com.bksoft.config;

import com.bksoft.domain.Product;
import com.bksoft.domain.Variant;
import com.bksoft.domain.Attribute;
import com.bksoft.domain.AttributeValue;
import com.bksoft.repository.ProductRepository;
import com.bksoft.repository.VariantRepository;
import com.bksoft.repository.AttributeRepository;
import com.bksoft.repository.AttributeValueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final VariantRepository variantRepo;
    private final AttributeRepository attributeRepo;
    private final AttributeValueRepository attrValRepo;

    public DataLoader(
        ProductRepository productRepo,
        VariantRepository variantRepo,
        AttributeRepository attributeRepo,
        AttributeValueRepository attrValRepo
    ) {
        this.productRepo = productRepo;
        this.variantRepo = variantRepo;
        this.attributeRepo = attributeRepo;
        this.attrValRepo = attrValRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // Xóa sạch dữ liệu cũ (nếu cần reset mỗi lần chạy)
        attrValRepo.deleteAll();
        variantRepo.deleteAll();
        productRepo.deleteAll();
        attributeRepo.deleteAll();

        // Dữ liệu mẫu
        String[] products = {"Áo khoác", "Áo phông"};
        String[] sizes = {"S", "M", "L", "XL"};
        String[] colors = {"đỏ", "trắng", "xanh", "vàng"};
        String[] materials = {"cotton", "lụa", "lilong"};

        // Tạo Attribute
        Attribute colorAttr = attributeRepo.save(new Attribute().name("màu"));
        Attribute sizeAttr = attributeRepo.save(new Attribute().name("size"));
        Attribute materialAttr = attributeRepo.save(new Attribute().name("chất liệu"));

        for (String pName : products) {
            Product product = new Product();
            product.setName(pName);
            product = productRepo.save(product);

            for (String size : sizes) {
                for (String color : colors) {
                    for (String material : materials) {
                        Variant variant = new Variant();
                        variant.setProduct(product);
                        variant.setPrice(BigDecimal.valueOf(100));
                        variant.setStock(10);
                        variant = variantRepo.save(variant);

                        // Tạo AttributeValue cho Variant
                        AttributeValue avSize = new AttributeValue();
                        avSize.setVariant(variant);
                        avSize.setAttribute(sizeAttr);
                        avSize.setValue(size);
                        attrValRepo.save(avSize);

                        AttributeValue avColor = new AttributeValue();
                        avColor.setVariant(variant);
                        avColor.setAttribute(colorAttr);
                        avColor.setValue(color);
                        attrValRepo.save(avColor);

                        AttributeValue avMaterial = new AttributeValue();
                        avMaterial.setVariant(variant);
                        avMaterial.setAttribute(materialAttr);
                        avMaterial.setValue(material);
                        attrValRepo.save(avMaterial);
                    }
                }
            }
        }

        System.out.println("✅ Dữ liệu mẫu EAV đã được tạo xong!");
    }
}
