package com.phamchien.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "db_user_base_product")
public class Product extends Auditable<String, String>{
    @Id
    @GenericGenerator(name = "ulidGenerator", strategy = "com.phamchien.service.id.UlidGenerator")
    @GeneratedValue(generator = "ulidGenerator")
    private String id;

    private String name;

    private String description;
}
