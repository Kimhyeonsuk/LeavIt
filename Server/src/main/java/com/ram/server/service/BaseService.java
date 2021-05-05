package com.ram.server.service;

import com.ram.server.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseService<Req,Res,Entity> implements CrudInterface<Req,Res> {
    @Autowired(required = true)
    protected JpaRepository<Entity,Long> baseRepository;
    //Entity 값으로 jparepository를 찾아서 주입시켜준다.
}
