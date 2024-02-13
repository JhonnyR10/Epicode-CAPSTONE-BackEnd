package Giovanni.Longo.EpicodeCAPSTONEBackEnd.repository;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.StatisticaGioco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticaGiocoRepository extends JpaRepository<StatisticaGioco, Long> {
}
