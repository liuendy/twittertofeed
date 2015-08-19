package uk.co.landbit.twittertofeed.social.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.co.landbit.twittertofeed.social.domain.UserConnection;
import uk.co.landbit.twittertofeed.social.domain.UserConnectionPK;

public interface UserConnectionRepository extends JpaRepository<UserConnection, UserConnectionPK> {

    List<UserConnection> findByPkProviderId(String providerId);
}
