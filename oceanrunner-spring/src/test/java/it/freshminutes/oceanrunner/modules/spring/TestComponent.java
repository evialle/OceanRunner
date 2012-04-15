/**
 * 
 */
package it.freshminutes.oceanrunner.modules.spring;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * @author Eric
 * 
 */
@Service("testService")
public class TestComponent {

	public int mustReturnOne() {
		return 1;
	}

}
