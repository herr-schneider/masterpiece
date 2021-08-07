package registry.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registry.entities.PostPackage;

import java.util.Optional;

@Repository
public interface PostPackageRepo extends JpaRepository<PostPackage, Long> {

    @Query("select p from PostPackage p where p.doku = :doku")
    Optional<PostPackage> findByDoku(@Param("doku") String doku);
}
