package imi.spring.backend.repositories;

import imi.spring.backend.models.LocationSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationSearchRepository extends JpaRepository<LocationSearch, Long> {
}
