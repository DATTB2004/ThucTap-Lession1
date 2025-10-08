package com.bksoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = { "attributeValues", "product" }, allowSetters = true)
    private Set<Variant> variants = new HashSet<>();

    // === Getters / Setters / Builders ===
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Product id(Long id) { this.id = id; return this; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Product name(String name) { this.name = name; return this; }

    public Set<Variant> getVariants() { return variants; }
    public void setVariants(Set<Variant> variants) { this.variants = variants; }
    public Product variants(Set<Variant> variants) { this.variants = variants; return this; }

    public Product addVariant(Variant v) { this.variants.add(v); v.setProduct(this); return this; }
    public Product removeVariant(Variant v) { this.variants.remove(v); v.setProduct(null); return this; }

    @Override
    public boolean equals(Object o) { return (this == o) || (o instanceof Product p && id != null && id.equals(p.id)); }
    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override public String toString() { return "Product{" + "id=" + id + ", name='" + name + '\'' + '}'; }
}
