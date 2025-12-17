package com.example.skai.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(length = 36)
    var id: String? = null,
    
    @Column(nullable = false, unique = true, length = 255)
    var email: String,
    
    @Column(nullable = false, length = 255)
    var password: String,
    
    @Column(nullable = false, length = 255)
    var name: String,
    
    @Column(length = 50)
    var phone: String? = null,
    
    @Column(length = 500)
    var address: String? = null,
    
    @Column(name = "is_admin")
    var isAdmin: Boolean = false,
    
    @Column(name = "profile_image_uri", length = 1000)
    var profileImageUri: String? = null
)

