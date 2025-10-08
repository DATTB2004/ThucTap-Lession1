package com.bksoft.web.rest;

import com.bksoft.domain.Variant;
import com.bksoft.service.VariantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/products/variant")
public class ProductVariantResource {

    private final VariantService variantService;

    public ProductVariantResource(VariantService variantService) {
        this.variantService = variantService;
    }

    // ✅ GET: tìm Variant theo 3 attribute
    @GetMapping("/{productId}/{sizeAttrId}/{colorAttrId}/{materialAttrId}")
    public ResponseEntity<Variant> getVariantByAttributeIds(
        @PathVariable Long productId,
        @PathVariable Long sizeAttrId,
        @PathVariable Long colorAttrId,
        @PathVariable Long materialAttrId
    ) {
        Optional<Variant> variant = variantService.findVariantByAttributeIds(productId, sizeAttrId, colorAttrId, materialAttrId);
        return variant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ POST: tạo Variant mới (trong product cụ thể)
    @PostMapping("/{productId}")
    public ResponseEntity<Variant> createVariant(
        @PathVariable Long productId,
        @RequestBody Variant variant,
        UriComponentsBuilder uriBuilder
    ) {
        Variant result = variantService.createVariant(productId, variant);
        URI location = uriBuilder.path("/api/products/variant/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    // ✅ PUT: cập nhật Variant
    @PutMapping("/{variantId}")
    public ResponseEntity<Variant> updateVariant(
        @PathVariable Long variantId,
        @RequestBody Variant variant
    ) {
        Variant updated = variantService.updateVariant(variantId, variant);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE: xóa Variant
    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable Long variantId) {
        variantService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }
}
