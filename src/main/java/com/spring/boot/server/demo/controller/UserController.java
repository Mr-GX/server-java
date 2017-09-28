package com.spring.boot.server.demo.controller;

import com.spring.boot.server.demo.advice.ApiAdviceHandler;
import com.spring.boot.server.demo.model.User;
import com.spring.boot.server.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private Environment env;
    @Autowired
    private UserRepository users;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ApiAdviceHandler<?> getUserList() {
        // This returns a JSON or XML with the user
        return new ApiAdviceHandler<>(HttpStatus.OK.value(), env.getProperty("success"), users.findAll());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ApiAdviceHandler<?> createUser(@ModelAttribute User user) {
        User save = users.save(user);
        if (save != null)
            return new ApiAdviceHandler<>(HttpStatus.OK.value(), env.getProperty("success"), null);
        else
            return new ApiAdviceHandler<>(HttpStatus.NOT_FOUND.value(), "save failed!", null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiAdviceHandler<?> getUserInfo(@PathVariable Long id) {
        User one = users.findOne(id);
        if (one != null)
            return new ApiAdviceHandler<>(HttpStatus.OK.value(), env.getProperty("success"), one);
        else
            return new ApiAdviceHandler<>(HttpStatus.NOT_FOUND.value(), "user not exist!", null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiAdviceHandler<?> updateUser(@PathVariable Long id, @ModelAttribute User user) {
        User u = users.findOne(id);
        u.setMobile(user.getMobile());
        u.setAge(user.getAge());
        User save = users.save(u);
        if (save != null)
            return new ApiAdviceHandler<>(HttpStatus.OK.value(), env.getProperty("success"), u.getId());
        else
            return new ApiAdviceHandler<>(HttpStatus.NOT_FOUND.value(), "update failed!", null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiAdviceHandler<?> delUser(@PathVariable Long id) {
        users.delete(id);
        return new ApiAdviceHandler<>(HttpStatus.OK.value(), env.getProperty("success"), null);
    }
}
