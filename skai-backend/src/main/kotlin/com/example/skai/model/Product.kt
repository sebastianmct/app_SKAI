package com.example.skai.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonProperty

@Entity
@Table(name = "products")
data class Product(
    @Id
    @Column(length = 36)
    var id: String? = null,
    
    @Column(nullable = false, length = 255)
    var name: String,
    
    @Lob
    @Column
    var description: String? = null,
    
    @Column(nullable = false)
    var price: Double,
    
    @Column(length = 100)
    var category: String? = null,
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_sizes", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "product_size", length = 20)
    var sizes: List<String>? = null,
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "image_url", length = 1000)
    var images: List<String>? = null,
    
    @Column
    var stock: Int? = null,
    
    @Column(name = "is_active")
    @JsonProperty("isActive")
    var isActive: Boolean = true,
    
    @Column(name = "created_at")
    var createdAt: Long? = null
)
