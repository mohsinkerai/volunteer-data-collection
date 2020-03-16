package com.mohsinkerai.adminlte;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mohsinkerai.adminlte.jamatkhana.JamatkhanaRepository;
import com.mohsinkerai.adminlte.users.MyUser;
import com.mohsinkerai.adminlte.users.MyUserService;
import com.mohsinkerai.adminlte.users.authority.MyAuthority;
import com.mohsinkerai.adminlte.users.authority.MyAuthorityRepository;

import java.io.InputStream;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@Profile("local")
public class UserCreator implements CommandLineRunner {

  private final MyUserService myUserService;
  private final JamatkhanaRepository jamatkhanaRepository;
  private final MyAuthorityRepository myAuthorityRepository;

  @Override
  public void run(String... args) throws Exception {
    CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
    CsvMapper mapper = new CsvMapper();
    InputStream is = UserCreator.class.getResourceAsStream("/users.csv");

    MappingIterator<LocalUser> readValues =
      mapper.reader(LocalUser.class).with(bootstrapSchema).readValues(is);
    List<LocalUser> localUsers = readValues.readAll();

    for(LocalUser user : localUsers) {
      String jk = user.getJk();
      com.mohsinkerai.adminlte.jamatkhana.Council jk_instance = jamatkhanaRepository.findByName(jk).orElseGet(() -> {
        return jamatkhanaRepository.save(new com.mohsinkerai.adminlte.jamatkhana.Council(jk));
      });

      MyAuthority user_authority = myAuthorityRepository.findByName("USER").get();

      MyUser myUser = new MyUser(user.getUsername().trim(), user.getPassword().trim(), true, false, false, false, jk_instance,
        Sets.newHashSet(jk_instance), Lists.newArrayList(user_authority), Lists.newArrayList());

      myUserService.save(myUser);
    }

    log.info("Users are {}", localUsers);
  }
}
