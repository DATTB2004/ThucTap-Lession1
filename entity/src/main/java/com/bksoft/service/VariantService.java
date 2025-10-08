package com.bksoft.service;

import com.bksoft.domain.Product;
import com.bksoft.domain.Variant;
import com.bksoft.repository.ProductRepository;
import com.bksoft.repository.VariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;

    public VariantService(VariantRepository variantRepository, ProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    public Optional<Variant> findVariantByAttributeIds(
        Long productId, Long sizeAttrId, Long colorAttrId, Long materialAttrId
    ) {
        return variantRepository.findVariantByAttributeIds(productId, sizeAttrId, colorAttrId, materialAttrId);
    }

    public Variant createVariant(Long productId, Variant variant) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        variant.setProduct(product);
        return variantRepository.save(variant);
    }

    public Variant updateVariant(Long variantId, Variant updatedVariant) {
        Variant existing = variantRepository.findById(variantId)
            .orElseThrow(() -> new RuntimeException("Variant not found: " + variantId));

        existing.setPrice(updatedVariant.getPrice());
        existing.setStock(updatedVariant.getStock());
        existing.setAttributeValues(updatedVariant.getAttributeValues());
        return variantRepository.save(existing);
    }

    public void deleteVariant(Long variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new RuntimeException("Variant not found: " + variantId);
        }
        variantRepository.deleteById(variantId);
    }
}
