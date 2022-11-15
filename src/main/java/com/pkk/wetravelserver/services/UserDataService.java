package com.pkk.wetravelserver.services;

import com.pkk.wetravelserver.model.UserData;
import com.pkk.wetravelserver.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserDataRepository userDataRepository;

    @Value("${entity.default-value.userdata.status}")
    private String defaultStatus;

    @Value("${entity.default-value.userdata.info}")
    private String defaultInfo;

    public UserData fillUserData(UserData data) throws Exception {
        if(!userDataRepository.existsById(data.getUser_id())){
            if (data.getInfo() == null){
                data.setInfo(defaultInfo);
            } else if (data.getStatus() == null){
                data.setStatus(defaultStatus);
            }
        }
        userDataRepository.save(data);
        return data;
    }
}
