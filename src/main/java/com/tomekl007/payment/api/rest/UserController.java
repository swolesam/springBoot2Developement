package com.tomekl007.payment.api.rest;

import com.tomekl007.payment.api.UserService;
import com.tomekl007.payment.domain.PaymentAndUser;
import com.tomekl007.payment.domain.User;
import com.tomekl007.payment.domain.UserDto;
import com.tomekl007.payment.infrastructure.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  // [selnasr] this is for testing purposes, push some data in via PostConstruct for TESTING.
  @PostConstruct
  public void insertUsers() {
    userService.insert(new User("T1", "m@m.pl"));
    userService.insert(new User("T2", "m2@m.pl"));
    userService.insert(new User("T3", "m3@m.pl"));
  }

  // [selnasr] Constructor injection
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  // [selnasr] getAllUsers endpoint
  // empty mapping "" means this is DEFAULT for the class. i.e "/users" w/ GET protocol will execute this function
  @GetMapping(value = "", produces = "application/json")
  public List<UserDto> getAllUsers() {
    LOG.info("Fetching all the users");

    /***
     * [SELNASR]
     * Important: Convert the Entity Classes to DTOs for transfer/client purposes.
     */
    // JAVA 8+ (Lambdas & Streams)
//    return userService.getAllUsers().stream().map(
//        u -> new UserDto(u.getId(), u.getName(), u.getEmail())
//    ).collect(Collectors.toList());

    // Pre-Java8
    // [selnasr]
    List<UserDto> userDtos = new ArrayList<UserDto>() ;
    List<User> users = userService.getAllUsers() ;
    for(User user : users) {
      UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail()) ;
      userDtos.add(userDto) ;
    }

    return userDtos ;
  }

  @GetMapping(value = "payments-for-user/{userId}", produces = "application/json")
  public PaymentAndUser paymentAndUsers(@PathVariable final String userId) throws UserNotFoundException {
    Optional<PaymentAndUser> paymentAndUsersForUserId = userService.getPaymentAndUsersForUserId(userId);
    if (!paymentAndUsersForUserId.isPresent()) {
      throw new UserNotFoundException("Payments for user id: " + userId + " not found");
    } else {
      return paymentAndUsersForUserId.get();
    }

  }


}
