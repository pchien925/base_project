package com.phamchien.model.criteria;

import com.phamchien.model.Settings;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class SettingsCriteria {
    private Long id;
    private String settingKey;
    private String groupName;
    private Integer isSystem;
    private Integer status;

    public static Specification<Settings> findSettingsByCriteria(final SettingsCriteria settingsCriteria) {
        return new Specification<Settings>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Settings> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (settingsCriteria.getId() != null) {
                    predicates.add(cb.equal(root.get("id"), settingsCriteria.getId()));
                }
                if (!StringUtils.isEmpty(settingsCriteria.getSettingKey())) {
                    predicates.add(cb.like(cb.lower(root.get("settingKey")), "%" + settingsCriteria.getSettingKey().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(settingsCriteria.getGroupName())) {
                    predicates.add(cb.like(cb.lower(root.get("groupName")), "%" + settingsCriteria.getGroupName().toLowerCase() + "%"));
                }
                if (settingsCriteria.getIsSystem() != null) {
                    predicates.add(cb.equal(root.get("isSystem"), settingsCriteria.getIsSystem()));
                }
                if (settingsCriteria.getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), settingsCriteria.getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
