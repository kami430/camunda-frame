package com.camunda.demo.base.utils;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RuleUtils {

    private final static String BASE_RULE_PATH = "rules/";

    private final static Logger LOGGER = LoggerFactory.getLogger(RuleUtils.class);

    private static KieServices kieServices = KieServices.Factory.get();

    public static KieSession newSession(String... paths) {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        Arrays.stream(paths).forEach(path -> kbuilder.add(ResourceFactory.newClassPathResource(BASE_RULE_PATH + (path.indexOf(".drl") != -1 ? path : path + ".drl")), ResourceType.DRL));
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                LOGGER.error("创建rule session失败", error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addPackages(kbuilder.getKnowledgePackages());
        return kbase.newKieSession();
    }

    public static KieSession pkgSession(String pkg) throws IOException {
        KieFileSystem kieFileSystem = kieFileSystem(pkg, false);
        KieContainer kieContainer = kieContainer(kieFileSystem);
        return kieSession(kieContainer);
    }

    public static KieSession pkgSession(String pkg, boolean deep) throws IOException {
        KieFileSystem kieFileSystem = kieFileSystem(pkg, deep);
        KieContainer kieContainer = kieContainer(kieFileSystem);
        return kieSession(kieContainer);
    }

    private static KieFileSystem kieFileSystem(String pkg, boolean deep) throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String locationPattern = "classpath*:" + BASE_RULE_PATH + (pkg != null ? pkg : "./") + (deep ? "/**" : "/") + "*.drl";
        final Resource[] resources = resolver.getResources(locationPattern);
        for (Resource file : resources) {
            kieFileSystem.write(ResourceFactory.newFileResource(file.getFile()));
        }
        return kieFileSystem;
    }

    private static KieContainer kieContainer(KieFileSystem kieFileSystem) {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    private static KieBase kieBase(KieContainer kieContainer) {
        return kieContainer.getKieBase();
    }

    private static KieSession kieSession(KieContainer kieContainer) {
        return kieContainer.newKieSession();
    }
}
