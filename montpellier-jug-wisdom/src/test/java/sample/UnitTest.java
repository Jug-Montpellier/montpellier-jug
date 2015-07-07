/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package sample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * A couple of unit tests.
 */
public class UnitTest {

    @Test
    public void simpleTest() {
        assertThat(32 + 10).isEqualTo(42);
    }

    /**
     * Checks that your controller is returning OK.
     */
    @Test
    public void testWelcome() throws Exception {

//        WelcomeController controller = new WelcomeController(new CartridgeSupport() {
//            @Override
//            public List<Cartridge> cartridges() {
//                return new ArrayList<>();
//            }
//        }, new NextEventSupport() {
//            @Override
//            public NextEvent getNextEvent() {
//                return null;
//            }
//        });
//        // Use a mock to simulate the template.
//        // You can do this for every service and template your controller is using.
//        controller.welcome = mock(Template.class);
//        Result result = controller.welcome();
//        assertThat(result.getStatusCode()).isEqualTo(Status.OK);
    }


   
   
}
