package com.srdcloud.taas.common.infra.nosqlpersist;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestCaseLanguageRepository extends MongoRepository<TestCaseLanguagePo, String> {
    TestCaseLanguagePo findByCaseId(long id);
}
