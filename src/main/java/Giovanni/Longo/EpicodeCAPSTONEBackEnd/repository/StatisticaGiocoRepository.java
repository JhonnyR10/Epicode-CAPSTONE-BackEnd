package Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.StatisticaGioco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticaGiocoRepository extends JpaRepository<StatisticaGioco, Long> {
    Optional<StatisticaGioco> findById(Long id);

    List<StatisticaGioco> findAllByNomeGioco(String nomeGioco);
}
