package Giovanni.Longo.EpicodeCAPSTONEBackEnd.model;

import Giovanni.Longo.EpicodeCAPSTONEBackEnd.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "utenti")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente")
    private long id;
    private String nome;
    private String cognome;
    private String password;
    private String email;
    private String username;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnoreProperties("utente")
    private List<StatisticaGioco> statisticheGiochi;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_matches",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    @JsonIgnoreProperties("matches")
    private Set<User> matches = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void removeStatisticaGioco(Long statisticaId) {
        // Rimuovi la statistica dal set di statistiche dell'utente
        statisticheGiochi.removeIf(statistica -> statistica.getIdStatisticaGioco().equals(statisticaId));
    }

    public Set<User> getMatches() {
        return matches;
    }

    public void addMatch(User match) {
        matches.add(match);
        match.getMatches().add(this);
    }

    public void removeMatch(User match) {
        matches.remove(match);
        match.getMatches().remove(this);
    }

}
