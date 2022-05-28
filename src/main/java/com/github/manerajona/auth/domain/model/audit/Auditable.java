package com.github.manerajona.auth.domain.model.audit;

public interface Auditable {
    Audit getAudit();

    void setAudit(Audit audit);
}
