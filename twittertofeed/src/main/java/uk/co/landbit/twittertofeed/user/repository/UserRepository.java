package uk.co.landbit.twittertofeed.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.landbit.twittertofeed.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String email);

}
