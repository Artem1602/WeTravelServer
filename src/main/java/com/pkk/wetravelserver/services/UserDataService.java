package com.pkk.wetravelserver.services;

import com.pkk.wetravelserver.model.User;
import com.pkk.wetravelserver.model.UserData;
import com.pkk.wetravelserver.repository.UserDataRepository;
import com.pkk.wetravelserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final UserRepository userRepository;

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
        User user = userRepository.findById(data.getUser_id())
                .orElseThrow(() -> new Exception("No user with id " + data.getUser_id()));
        data.setUser(user);
        user.setUserData(data);
        userDataRepository.save(data);
        return data;
    }
}
