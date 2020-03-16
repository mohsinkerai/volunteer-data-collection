package com.mohsinkerai.adminlte.base;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleBaseRepository<E extends BaseEntity> extends BaseRepository<E, Long> {

}
