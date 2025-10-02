package com.bksoft.repository;

import com.bksoft.domain.entity.User;
import com.bksoft.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // thêm mới user
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // sửa uẻ
    @Override
    public User updateUser(Long id, User user) {
        User up = userRepository.findById(id).orElseThrow(() -> new RuntimeException("không tìm thấy user"));
        up.setUserName(user.getUserName());
        up.setPassword(user.getPassword());
        return userRepository.save(up);
    }

    // xóa user
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // lấy thông tin user
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("không tìm thấy user"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
