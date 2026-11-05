package com.umc.movie.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@MappedSuperclass
public abstract class BaseTimeEntity {
 @CreationTimestamp @Column(name="created_at",nullable=false,updatable=false)
 private LocalDateTime createdAt;
 @UpdateTimestamp @Column(name="updated_at",nullable=false)
 private LocalDateTime updatedAt;
 public LocalDateTime getCreatedAt(){return createdAt;}
 public LocalDateTime getUpdatedAt(){return updatedAt;}
}
