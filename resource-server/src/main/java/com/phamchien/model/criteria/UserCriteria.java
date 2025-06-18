package com.phamchien.model.criteria;

import com.phamchien.model.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCriteria {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Integer status;
    public Specification<User> getSpecification() {
        return new Specification<User>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId()!=null)
                {
                    predicates.add(cb.equal(root.get("id"),getId()));
                }
                if(getStatus()!=null)
                {
                    predicates.add(cb.equal(root.get("status"),getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
