package com.company.example.service;

import com.company.example.entity.OutgoingDocuments;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service(CleanDbService.NAME)
public class CleanDbServiceBean implements CleanDbService {

    @Inject
    private DataManager dataManager;
    @Inject
    private Persistence persistence;

    @Override
    public void cleanDb() {
        List<OutgoingDocuments> docs = dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                "select e from example$OutgoingDocuments e where e.deleteTs IS NOT NULL or e.deletedBy IS NOT NULL")).setView("main-outgoingDocuments-view").setSoftDeletion(false));
        for (OutgoingDocuments doc : docs) {
            List<ProcInstance> procInstances = dataManager.loadList(LoadContext.create(ProcInstance.class).setSoftDeletion(false).setQuery(LoadContext.createQuery(
                    "select e from bpm$ProcInstance e where e.entity.entityId = :id").setParameter("id", doc.getId()))
                    .setView("procInstance-full"));
            if (procInstances != null && !procInstances.isEmpty()) {
                for (ProcInstance procInstance : procInstances) {
                    List<ProcTask> procTasks = dataManager.loadList(LoadContext.create(ProcTask.class).setSoftDeletion(false).setQuery(LoadContext.createQuery(
                            "select e from bpm$ProcTask e where e.procInstance.id = :id").setParameter("id", procInstance.getId()))
                            .setView("procTask-view"));
                    if (procTasks != null && !procTasks.isEmpty()) {
                        for (ProcTask task : procTasks) {
                            deleteEntity(task);
                        }
                    }
                    List<ProcActor> procActors = dataManager.loadList(LoadContext.create(ProcActor.class).setSoftDeletion(false).setQuery(LoadContext.createQuery(
                            "select e from bpm$ProcActor e where e.procInstance.id = :id").setParameter("id", procInstance.getId()))
                            .setView("procActor-view"));
                    if(procActors != null && !procActors.isEmpty()){
                        for(ProcActor actor : procActors){
                            deleteEntity(actor);
                        }
                    }
                }
            }
            List<ProcInstance> freshProcInstances = dataManager.loadList(LoadContext.create(ProcInstance.class).setSoftDeletion(false).setQuery(LoadContext.createQuery(
                    "select e from bpm$ProcInstance e where e.entity.entityId = :id").setParameter("id", doc.getId()))
                    .setView("procInstance-full"));
            if(freshProcInstances != null && !freshProcInstances.isEmpty()){
                for(ProcInstance procInstance : freshProcInstances){
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