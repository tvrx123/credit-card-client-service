package hr.rba.credit_card_client_service.feature.creation_audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class CreationAudit {

  @CreatedBy
  @Column(name = "user_created")
  private String userCreated;

  @LastModifiedBy
  @Column(name = "user_modified")
  private String userModified;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime dateCreated;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime dateModified;
}
