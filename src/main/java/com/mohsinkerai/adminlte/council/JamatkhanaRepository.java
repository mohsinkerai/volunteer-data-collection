package com.mohsinkerai.adminlte.jamatkhana;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface JamatkhanaRepository extends SimpleBaseRepository<Jamatkhana> {

  Optional<Jamatkhana> findByName(String name);
}
