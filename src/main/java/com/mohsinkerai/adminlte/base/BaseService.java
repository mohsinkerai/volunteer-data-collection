package com.mohsinkerai.adminlte.base;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class BaseService<E extends BaseEntity, I extends Serializable> {

  protected final BaseRepository<E, I> simpleBaseRepository;

  protected BaseService(BaseRepository<E, I> simpleBaseRepository) {
    this.simpleBaseRepository = simpleBaseRepository;
  }
  public List<E> findAll() {
    return Lists.newArrayList(simpleBaseRepository.findAll());
  }

  public Page<E> findAll(Pageable pageable) {
    return simpleBaseRepository.findAll(pageable);
  }

  public Optional<E> findOne(I id) {
    return simpleBaseRepository.findById(id);
//    return Optional.ofNullable(simpleBaseRepository.findOne(id));
  }

  public E save(E e) {
    return simpleBaseRepository.save(e);
  }

  public void delete(E e) {
    simpleBaseRepository.delete(e);
  }

  public void delete(I id) {
    simpleBaseRepository.deleteById(id);
//    simpleBaseRepository.delete(id);
  }
}
