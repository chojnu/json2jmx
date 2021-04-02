package com.srdcloud.taas.common.infra.persist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCasePo, Long> {
    TestCasePo findById(long id);
}
