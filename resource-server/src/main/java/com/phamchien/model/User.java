package com.phamchien.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "db_user_base_user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.phamchien.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private Date birthday;

    @Column(name = "account_id")
    private Long accountId;
}
