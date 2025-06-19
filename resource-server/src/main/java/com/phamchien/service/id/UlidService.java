package com.phamchien.service.id;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;

public class UlidService {
    public static UlidService instance;
    private UlidService() {

    }

    public synchronized static UlidService getInstance() {
        if(instance == null){
            instance = new UlidService();
        }
        return instance;
    }

    public String nextId() {
        return UlidCreator.getUlid().toString();
    }
}
