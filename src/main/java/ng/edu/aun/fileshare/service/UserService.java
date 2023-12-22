package ng.edu.aun.fileshare.service;

import java.util.List;
import java.util.Optional;

import ng.edu.aun.fileshare.model.User;

public interface UserService {
    public void save(User user);

    public User findByUsername(String un);

    public User getCurrentUser();

    List<User> get();

    void deleteById(Long id);

    Optional<User> findById(Long id);

    public long countUsers();
}
