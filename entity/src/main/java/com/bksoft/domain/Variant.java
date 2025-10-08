package com.bksoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "variant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Variant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "variant", "attribute" }, allowSetters = true)
    private Set<AttributeValue> attributeValues = new HashSet<>();

    // === Getters / Setters / Builders ===
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Variant id(Long id) {
        this.id = id; return this;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Variant price(BigDecimal price) {
        this.price = price; return this;
    }

    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Variant stock(Integer stock) {
        this.stock = stock; return this;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Variant product(Product product) {
        this.product = product; return this;
    }

    public Set<AttributeValue> getAttributeValues() {
        return attributeValues;
    }
    public void setAttributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }
    public Variant attributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues; return this;
    }


    // ✅ Hai hàm cần thêm cho test JHipster
    public Variant addAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
        attributeValue.setVariant(this);
        return this;
    }

    public Variant removeAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.remove(attributeValue);
        attributeValue.setVariant(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variant)) return false;
        return id != null && id.equals(((Variant) o).id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override
    public String toString() {
        return "Variant{" +
            "id=" + id +
            ", price=" + price +
            ", stock=" + stock +
            '}';
    }

}
