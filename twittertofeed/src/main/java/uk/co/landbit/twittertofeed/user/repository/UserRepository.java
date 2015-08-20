package uk.co.landbit.twittertofeed.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findBySignInProvider(SignInProvider provider);

}
