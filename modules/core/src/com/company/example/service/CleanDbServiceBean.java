package com.company.example.service;

import com.company.example.entity.*;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(CleanDbService.NAME)
public class CleanDbServiceBean implements CleanDbService {

    @Inject
    private DataManager dataManager;
    @Inject
    private Persistence persistence;

    @Override
    public void cleanDb() {
        cleanOutDocAndTasks();
        cleanAffairNomenclature();
        cleanOrganizations();
        cleanRegistrationLogs();
        cleanSubDivisions();
        cleanWorkers();
        cleanDocumentTypes();

    }

    private void cleanWorkers() {
        List<Workers> workers = dataManager.loadList(LoadContext.create(Workers.class).setQuery(LoadContext.createQuery(
                "select e from example$Workers e where e.deleteTs is not null")).setSoftDeletion(false));
        if (workers != null) {
            for (Workers worker : workers) {
                List<SubDivision> depHeadDivisions = dataManager.loadList(LoadContext.create(SubDivision.class).setQuery(LoadContext.createQuery(
                        "select e from example$SubDivision e where e.departament_head.id = :id").setParameter("id", worker.getId())).setSoftDeletion(false));
                if (depHeadDivisions != null) {
                    for (SubDivision div : depHeadDivisions) {
                        try (Transaction tx = persistence.getTransaction()) {
                            EntityManager em = persistence.getEntityManager();
                            SubDivision sd = em.find(SubDivision.class, div.getId());
                            sd.setDepartament_head(null);
                            em.persist(sd);
                            tx.commit();
                        }
                    }
                }
                Map<String, Object> params = new HashMap<>();
                params.put("signId", worker.getId());
                params.put("execId", worker.getId());
                List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                        "select e from example$OutgoingDocuments e left join e.sign sign left join e.executor executor where sign.id = :signId or executor.id = :execId").setParameters(params)).setView("deleteDoc-view"));
                if (docs != null) {
                    for (OutgoingDocuments doc : docs) {
                        try (Transaction tx = persistence.getTransaction()) {
                            EntityManager em = persistence.getEntityManager();
                            OutgoingDocuments d = em.find(OutgoingDocuments.class, doc.getId(), View.LOCAL);
                            if (d.getSign() != null && d.getSign().getId().equals(worker.getId())) {
                                d.setSign(null);
                            }
                            if (d.getExecutor() != null && d.getExecutor().getId().equals(worker.getId())) {
                                d.setExecutor(null);
                            }
                            em.persist(d);
                            tx.commit();
                        }
                    }
                }
                deleteEntity(worker);
            }
        }
        persistence.setSoftDeletion(true);
    }

    private void cleanSubDivisions() {
        List<SubDivision> divisions = dataManager.loadList(LoadContext.create(SubDivision.class).setQuery(LoadContext.createQuery(
                "select e from example$SubDivision e where e.deleteTs is not null")).setSoftDeletion(false));
        if (divisions != null) {
            for (SubDivision div : divisions) {
                List<Workers> workers = dataManager.loadList(LoadContext.create(Workers.class).setQuery(LoadContext.createQuery(
                        "select e from example$Workers e left join e.sub_division sub_division where sub_division.id = :id").setParameter("id", div.getId())).setSoftDeletion(false));
                List<SubDivision> leadDivisions = dataManager.loadList(LoadContext.create(SubDivision.class).setQuery(LoadContext.createQuery(
                        "select e from example$SubDivision e left join e.lead_subdivision lead_subdivision where lead_subdivision.id = :id").setParameter("id", div.getId())).setSoftDeletion(false));
                if (workers != null) {
                    for (Workers w : workers) {
                        try (Transaction tx = persistence.getTransaction()) {
                            EntityManager em = persistence.getEntityManager();
                            Workers wk = em.find(Workers.class, w.getId());
                            wk.setSub_division(null);
                            em.persist(wk);
                            tx.commit();
                        }
                    }
                }
                if (leadDivisions != null) {
                    for (SubDivision sd : leadDivisions) {
                        try (Transaction tx = persistence.getTransaction()) {
                            EntityManager em = persistence.getEntityManager();
                            SubDivision subd = em.find(SubDivision.class, sd.getId());
                            subd.setLead_subdivision(null);
                            em.persist(subd);
                            tx.commit();
                        }
                    }
                }
                deleteEntity(div);
            }
        }
        persistence.setSoftDeletion(true);
    }

    private void cleanRegistrationLogs() {
        List<RegistrationLogs> logs = dataManager.loadList(LoadContext.create(RegistrationLogs.class).setQuery(LoadContext.createQuery(
                "select e from example$RegistrationLogs e where e.deleteTs is not null")).setSoftDeletion(false));
        if (logs != null) {
            for (RegistrationLogs log : logs) {
                List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                        "select e from example$OutgoingDocuments e where e.log.id = :id").setParameter("id", log.getId())).setSoftDeletion(false));
                if (docs != null) {
                    for (OutgoingDocuments doc : docs) {
                        try (Transaction tx = persistence.getTransaction()) {
                            EntityManager em = persistence.getEntityManager();
                            OutgoingDocuments d = em.find(OutgoingDocuments.class, doc.getId());
                            d.setLog(null);
                            em.persist(d);
                            tx.commit();
                        }
                    }
                }
                deleteEntity(log);
            }
        }
        persistence.setSoftDeletion(true);

    }

    private void cleanDocumentTypes() {
        List<DocumentTypes> documentTypes = dataManager.loadList(LoadContext.create(DocumentTypes.class).setQuery(LoadContext.createQuery(
                "select e from example$DocumentTypes e where e.deleteTs is not null")).setSoftDeletion(false));
        if (documentTypes != null) {
            for (DocumentTypes org : documentTypes) {
                List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                        "select e from example$OutgoingDocuments e where e.document_type.id = :id").setParameter("id", org.getId())).setSoftDeletion(false));
                for (OutgoingDocuments doc : docs) {
                    try (Transaction tx = persistence.getTransaction()) {
                        EntityManager em = persistence.getEntityManager();
                        OutgoingDocuments d = em.find(OutgoingDocuments.class, doc.getId());
                        d.setDocument_type(null);
                        em.persist(d);
                        tx.commit();
                    }
                }
                deleteEntity(org);
            }
        }
        persistence.setSoftDeletion(true);
    }

    private void cleanOrganizations() {
        List<Organizations> organizations = dataManager.loadList(LoadContext.create(Organizations.class).setQuery(LoadContext.createQuery(
                "select e from example$Organizations e where e.deleteTs is not null")).setSoftDeletion(false));
        if (organizations != null) {
            for (Organizations org : organizations) {
                List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                        "select e from example$OutgoingDocuments e where e.addressee.id = :id").setParameter("id", org.getId())));
                for (OutgoingDocuments doc : docs) {
                    try (Transaction tx = persistence.getTransaction()) {
                        EntityManager em = persistence.getEntityManager();
                        OutgoingDocuments d = em.find(OutgoingDocuments.class, doc.getId());
                        d.setAddressee(null);
                        d.setFull_name(null);
                        em.persist(d);
                        tx.commit();
                    }
                }
                deleteEntity(org);
            }
        }
        persistence.setSoftDeletion(true);
    }

    private void cleanAffairNomenclature() {
        List<AffairsNomenclature> affairs = dataManager.loadList(LoadContext.create(AffairsNomenclature.class).setQuery(LoadContext.createQuery(
                "select e from example$AffairsNomenclature e where e.deleteTs is not null")).setSoftDeletion(false));
        if (affairs != null) {
            for (AffairsNomenclature affair : affairs) {
                List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setSoftDeletion(false).setQuery(LoadContext.createQuery(
                        "select e from example$OutgoingDocuments e where e.affair.id = :id").setParameter("id", affair.getId())));
                for (OutgoingDocuments doc : docs) {
                    try (Transaction tx = persistence.getTransaction()) {
                        EntityManager em = persistence.getEntityManager();
                        OutgoingDocuments d = em.find(OutgoingDocuments.class, doc.getId());
                        d.setAffair(null);
                        d.setAffair_date(null);
                        em.persist(d);
                        tx.commit();
                    }
                }
                deleteEntity(affair);
            }
        }
        persistence.setSoftDeletion(true);
    }

    private void cleanOutDocAndTasks() {
        List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                "select e from example$OutgoingDocuments e where e.deleteTs IS NOT NULL")).setSoftDeletion(false));
        for (OutgoingDocuments doc : docs) {
            List<ProcInstance> procInstances = dataManager.loadList(LoadContext.create(ProcInstance.class).setQuery(LoadContext.createQuery(
                    "select e from bpm$ProcInstance e where e.entity.entityId = :id").setParameter("id", doc.getId())).setSoftDeletion(false)
                    .setView("procInstance-full"));
            if (procInstances != null && !procInstances.isEmpty()) {
                for (ProcInstance procInstance : procInstances) {
                    List<ProcTask> procTasks = dataManager.loadList(LoadContext.create(ProcTask.class).setQuery(LoadContext.createQuery(
                            "select e from bpm$ProcTask e where e.procInstance.id = :id").setParameter("id", procInstance.getId())).setSoftDeletion(false));
                    if (procTasks != null && !procTasks.isEmpty()) {
                        for (ProcTask task : procTasks) {
                            deleteEntity(task);
                        }
                    }
                    List<ProcActor> procActors = dataManager.loadList(LoadContext.create(ProcActor.class).setQuery(LoadContext.createQuery(
                            "select e from bpm$ProcActor e where e.procInstance.id = :id").setParameter("id", procInstance.getId())).setSoftDeletion(false));
                    if (procActors != null && !procActors.isEmpty()) {
                        for (ProcActor actor : procActors) {
                            deleteEntity(actor);
                        }
                    }
                }
            }
            List<ProcInstance> freshProcInstances = dataManager.loadList(LoadContext.create(ProcInstance.class).setSoftDeletion(false).setQuery(LoadContext.createQuery(
                    "select e from bpm$ProcInstance e where e.entity.entityId = :id").setParameter("id", doc.getId()))
                    .setView("procInstance-full"));
            if (freshProcInstances != null && !freshProcInstances.isEmpty()) {
                for (ProcInstance procInstance : freshProcInstances) {
                    deleteEntity(procInstance);
                }
            }
            deleteEntity(doc);
        }
        persistence.setSoftDeletion(true);
    }

    private void deleteEntity(Entity entity) {
        persistence.setSoftDeletion(false);
        try (Transaction tx = persistence.getTransaction()) {
            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(false);
            em.remove(entity);
            tx.commit();
        }
    }

    private void deleteEntity(Entity entity, boolean softDeletion) {
        persistence.setSoftDeletion(softDeletion);
        try (Transaction tx = persistence.getTransaction()) {
            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(false);
            em.remove(entity);
            tx.commit();
        }
    }
}