package br.ufscar.dc.dsw.mural.repositories;

import br.ufscar.dc.dsw.mural.domain.entities.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByUserUsername(String username);

    @Query("SELECT m FROM Message m ORDER BY m.id DESC")
    List<Message> findAllOrderedByIdDesc();
}
