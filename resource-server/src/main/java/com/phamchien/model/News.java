package com.phamchien.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_user_base_news")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class News extends Auditable<String, Long> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.phamchien.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "banner")
    private String banner;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @ManyToOne
    @JoinColumn(name ="category_id")
    private Category category;

    @Column(name = "pin_top")
    private Integer pinTop = 0;
}
