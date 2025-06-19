package com.phamchien.service.id;

import com.phamchien.model.ReuseId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;
import java.util.UUID;


public class UlidGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        try{
            ReuseId<String> reuseId = (ReuseId) object;
            if(reuseId.getReusedId() != null && !reuseId.getReusedId().trim().isEmpty()){
                return reuseId.getReusedId();
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
        return UlidService.getInstance().nextId();
    }

    public String nextId(){
        return UlidService.getInstance().nextId();
    }

}
