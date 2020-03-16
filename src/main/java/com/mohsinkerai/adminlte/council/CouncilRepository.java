package com.mohsinkerai.adminlte.council;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CouncilRepository extends SimpleBaseRepository<Council> {

  Optional<Council> findByName(String name);
}
