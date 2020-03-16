package com.mohsinkerai.adminlte.base;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class SimpleBaseService<E extends BaseEntity> extends BaseService<E, Long>{

  protected SimpleBaseService(
    BaseRepository<E, Long> simpleBaseRepository) {
    super(simpleBaseRepository);
  }
}
