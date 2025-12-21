package com.zgqf.house.service;

import com.zgqf.house.entity.User;
import java.util.Map;

public interface UserService {
    /**
     * User login
     * @param username username
     * @param password password
     * @return User object if successful, null otherwise
     */
    User login(String username, String password);

    /**
     * Register a new user
     * @param user User basic info
     * @param additionalInfo Additional info (Buyer or Seller details)
     * @return Registered User
     */
    User register(User user, Map<String, Object> additionalInfo);

    /**
     * Get user by username
     * @param username username
     * @return User object
     */
    User getUserByUsername(String username);
}
