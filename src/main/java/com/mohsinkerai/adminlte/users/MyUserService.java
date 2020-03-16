package com.mohsinkerai.adminlte.users;

import com.google.errorprone.annotations.concurrent.LazyInit;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import com.mohsinkerai.adminlte.users.authority.MyAuthority;
import com.mohsinkerai.adminlte.users.authority.MyAuthorityService;
import com.mohsinkerai.adminlte.users.authority.MyAuthoritySwitchDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class MyUserService extends SimpleBaseService<MyUser> implements UserDetailsService,
  UserDetailsManager {

  private final MyAuthorityService myAuthorityService;
  private final MyUserRepository myUserRepository;
  private final PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;

  public MyUserService(
    MyAuthorityService myAuthorityService,
    MyUserRepository myUserRepository,
    PasswordEncoder passwordEncoder) {
    super(myUserRepository);
    this.myAuthorityService = myAuthorityService;
    this.myUserRepository = myUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public Optional<MyUser> findOne(Long id) {
    return super.findOne(id);
//      .map(this::populateAuthoritySwitch);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return myUserRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException("Unable to find username " + username));
  }

  public Optional<MyUser> findByUsername(String username) {
    return myUserRepository.findByUsername(username);
  }

  @Override
  public MyUser save(MyUser myUser) {
    if (!StringUtils.isEmpty(myUser.getPassword())) {
      myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
    } else if (myUser.getId() != null) {
      MyUser user = findOne(myUser.getId()).get();
      myUser.setPassword(user.getPassword());
    } else {
      throw new ValidationException("Empty Password");
    }
    return super.save(myUser);
  }

  @Override
  public void createUser(UserDetails user) {
    save(user);
  }

  @Override
  public void updateUser(UserDetails user) {
    save(user);
  }

  @Override
  public void deleteUser(String username) {
    findByUsername(username).ifPresent(user -> {
      user.setEnabled(false);
      super.save(user);
    });
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
    Authentication currentUser = SecurityContextHolder.getContext()
      .getAuthentication();

    if (currentUser == null) {
      // This would indicate bad coding somewhere
      throw new AccessDeniedException(
        "Can't change password as no Authentication object found in context "
          + "for current user.");
    }

    String username = currentUser.getName();

    // If an authentication manager has been set, re-authenticate the user with the
    // supplied password.
    if (authenticationManager != null) {
      log.debug("Reauthenticating user '" + username
        + "' for password change request.");

      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        username, oldPassword));
    } else {
      log.debug("No authentication manager set. Password won't be re-checked.");
    }

    log.debug("Changing password for user '" + username + "'");

    MyUser myUser = findByUsername(username).get();

    myUser.setPassword(newPassword);

    SecurityContextHolder.getContext().setAuthentication(
      createNewAuthentication(currentUser, myUser));
  }

  @Override
  public boolean userExists(String username) {
    return findByUsername(username).isPresent();
  }

  private void save(UserDetails user) {
    if (user instanceof MyUser) {
      save((MyUser) user);
      return;
    }

    Optional<MyUser> optionalMyUser = findByUsername(user.getUsername());
    MyUser myUser = optionalMyUser.orElse(new MyUser());
    myUser.apply(user);
    save(myUser);
  }

  public void setAuthenticationManager(
    AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public MyUser getCurrentLoggedInUser() {
    MyUser principal = (MyUser) SecurityContextHolder.getContext().getAuthentication()
      .getPrincipal();
    String username = principal.getUsername();
    return findByUsername(username).get();
  }

  protected Authentication createNewAuthentication(Authentication currentAuth, MyUser myUser) {
    UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
      myUser.getUsername(), null, myUser.getAuthorities());
    newAuthentication.setDetails(currentAuth.getDetails());

    return newAuthentication;
  }

  private MyUser populateAuthoritySwitch(MyUser myUser) {
    Map<String, MyAuthority> authorityMap = myUser
      .getAuthorities()
      .stream()
      .map(MyAuthority.class::cast)
      .collect(Collectors.toMap(MyAuthority::getAuthority, Function.identity()));

    List<MyAuthoritySwitchDto> authoritySwitchDtos = myAuthorityService.findAll().stream()
      .map(myAuthority -> new MyAuthoritySwitchDto(myAuthority,
        authorityMap.containsKey(myAuthority.getName())))
      .collect(Collectors.toList());

    myUser.setAuthoritySwitchDtos(authoritySwitchDtos);
    return myUser;
  }
}
