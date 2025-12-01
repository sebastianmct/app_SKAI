package com.example.skai.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    var id: String? = null,
    
    @Column(nullable = false, unique = true)
    var email: String,
    
    @Column(nullable = false)
    var password: String,
    
    @Column(nullable = false)
    var name: String,
    
    var phone: String? = null,
    
    var address: String? = null,
    
    @Column(name = "is_admin")
    var isAdmin: Boolean = false,
    
    @Column(name = "profile_image_uri")
    var profileImageUri: String? = null
)

