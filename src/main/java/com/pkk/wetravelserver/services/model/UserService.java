package com.pkk.wetravelserver.services.model;

import com.pkk.wetravelserver.model.User;
import com.pkk.wetravelserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void saveUserImgPath(Long userId, String imgPath){
        User user = userRepository.getById(userId);
        user.setImgPath(imgPath);
        userRepository.save(user);
    }
}
