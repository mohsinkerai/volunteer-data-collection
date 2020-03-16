package com.mohsinkerai.adminlte.base;

import com.mohsinkerai.adminlte.base.BaseEntity;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<E extends BaseEntity, I extends Serializable> extends JpaRepository<E, I> {

}
