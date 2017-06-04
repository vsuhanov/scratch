/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scratch

import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class ScratchTest {
    @Test fun creatingScratches() {
        var scratch = Scratch.create("scratch.txt")
        assertThat(scratch.name, equalTo("scratch"))
        assertThat(scratch.extension, equalTo("txt"))

        scratch = Scratch.create("&scratch.txt")
        assertThat(scratch.name, equalTo("scratch"))
        assertThat(scratch.extension, equalTo("txt"))

        scratch = Scratch.create("scratch.t&xt")
        assertThat(scratch.name, equalTo("scratch"))
        assertThat(scratch.extension, equalTo("txt"))

        scratch = Scratch.create("scratch")
        assertThat(scratch.name, equalTo("scratch"))
        assertThat(scratch.extension, equalTo(""))
    }
}