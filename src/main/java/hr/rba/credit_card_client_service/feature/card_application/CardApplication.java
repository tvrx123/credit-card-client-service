package hr.rba.credit_card_client_service.feature.card_application;

import hr.rba.credit_card_client_service.feature.creation_audit.CreationAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "CARD_APPLICATIONS")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CardApplication extends CreationAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "OIB", nullable = false)
  private String oib;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  private Status status;

  @AllArgsConstructor
  public enum Status {
    Pending,
    Approved,
    Rejected
  }
}
