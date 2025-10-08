package com.bksoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "attribute_value")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributeValues" }, allowSetters = true)
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributeValues", "product" }, allowSetters = true)
    private Variant variant;

    // === Getters / Setters / Builders ===
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AttributeValue id(Long id) {
        this.id = id; return this;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public AttributeValue value(String value) {
        this.value = value; return this;
    }

    public Attribute getAttribute() {
        return attribute;
    }
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
    public AttributeValue attribute(Attribute attribute) {
        this.attribute = attribute; return this;
    }

    public Variant getVariant() {
        return variant;
    }
    public void setVariant(Variant variant) {
        this.variant = variant;
    }
    public AttributeValue variant(Variant variant) {
        this.variant = variant; return this;
    }

    @Override public boolean equals(Object o) { return (this == o) || (o instanceof AttributeValue a && id != null && id.equals(a.id)); }
    @Override public int hashCode() { return getClass().hashCode(); }

    @Override public String toString() { return "AttributeValue{" + "id=" + id + ", value='" + value + '\'' + '}'; }
}
