package com.vmware.drools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.*;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.util.List;

public class DroolsBeanFactory {

    private List<String> ruleFiles;

    //If pool
    private KieContainerSessionsPool pool;

    public DroolsBeanFactory(List<String> ruleFiles) {
        this.ruleFiles = ruleFiles;
    }

    private KieServices kieServices=KieServices.Factory.get();

    public KieSession getKieSession() throws IOException {
        return getKieContainer().newKieSession();
    }

    public StatelessKieSession getStatelessKieSession() throws IOException {
        return getKieContainer().newStatelessKieSession();
    }

    public StatelessKieSession getStatelessKieSessionFromPool() throws IOException {
        // Obtain a KIE session pool from the KIE container
        KieContainerSessionsPool pool = getKieContainer().newKieSessionsPool(10);

        // Create KIE sessions from the KIE session pool
        return pool.newStatelessKieSession();
    }

    public void destroyKieSessionPool() {
        if(pool != null)
            pool.shutdown();
    }

    public KieContainer getKieContainer() throws IOException {
        getKieRepository();

        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem(ruleFiles));
        kb.buildAll();

        KieModule kieModule = kb.getKieModule();
        KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());

        return kContainer;

    }

    private  KieFileSystem getKieFileSystem(List<String> ruleFiles) throws IOException{
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        List<String> rules= ruleFiles;
        for(String rule:rules){
            kieFileSystem.write(ResourceFactory.newClassPathResource(rule));
        }
        return kieFileSystem;

    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }
}
