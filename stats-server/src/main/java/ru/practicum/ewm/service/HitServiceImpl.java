package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.ViewStats;
import ru.practicum.ewm.repository.HitRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Autowired
    public HitServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public Hit save(Hit hit) {
        return hitRepository.save(hit);
    }

    @Override
    public List<ViewStats> findStats(String[] uris, LocalDateTime start, LocalDateTime end, Boolean unique) {
        ViewStats viewStats = new ViewStats();
        List<ViewStats> result2 = null;

        List<Hit> result = null;
        Specification querySpec = new Specification<Hit>() {
            @Override
            public Predicate toPredicate(Root<Hit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.between(root.get("request_time"), start, end));
                if (uris != null) {
                    predicates.add(cb.equal(root.get("uri"), uris));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        result = this.hitRepository.findAll(querySpec);
        viewStats.setUri(result.get(0).getUri());
        viewStats.setApp(result.get(0).getApp());
        if (unique) {
            List<String> ips = result.stream().map(Hit::getIp).distinct().collect(Collectors.toList());
            viewStats.setHits(ips.size());
        } else {
            viewStats.setHits(result.size());
        }
        return result2;

        /*
        CriteriaQuery<String> query = .......
    Root<Employee> employee = query.from(Employee.class);
    query.select(employee.get(Employee_.name))
         .distinct(true);

        List<Hit> result = hitRepository.findAllByUriAndTimestampBetween(uri, start, end);
        ViewStats viewStats = new ViewStats();
        viewStats.setUri(uri);
        viewStats.setApp(result.get(0).getApp());
        if (unique){
            List<String> ips = result.stream().map(Hit::getIp).distinct().collect(Collectors.toList());
            viewStats.setHits(ips.size());
        }
        else {
            viewStats.setHits(result.size());
        }
        return viewStats; */
    }

}
