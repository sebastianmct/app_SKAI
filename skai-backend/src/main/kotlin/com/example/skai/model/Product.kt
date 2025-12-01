package com.example.skai.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonProperty

@Entity
@Table(name = "products")
data class Product(
    @Id
    var id: String? = null,
    
    @Column(nullable = false)
    var name: String,
    
    @Column(columnDefinition = "TEXT")
    var description: String? = null,
    
    @Column(nullable = false)
    var price: Double,
    
    var category: String? = null,
    
    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "size")
    var sizes: List<String>? = null,
    
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "image_url")
    var images: List<String>? = null,
    
    var stock: Int? = null,
    
    @Column(name = "is_active")
    @JsonProperty("isActive")
    var isActive: Boolean = true,
    
    @Column(name = "created_at")
    var createdAt: Long? = null
)
