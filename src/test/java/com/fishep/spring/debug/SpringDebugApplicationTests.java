package com.fishep.spring.debug;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SpringDebugApplicationTests {

	@Test
	void doTest() {
		log.info("doTest begin");
		log.info("doTest end");
	}

}
