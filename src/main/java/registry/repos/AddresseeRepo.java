package registry.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registry.dtos.AddresseeDto;
import registry.entities.Addressee;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddresseeRepo extends JpaRepository<Addressee, Long> {


    //List<Addressee> findByName(Optional<String> name);

    @Query("select a from Addressee a where a.name like :name")
    List<Addressee> findByName(@Param("name") Optional<String> name);
}