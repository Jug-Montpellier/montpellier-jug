package org.jug.montpellier.forms.services.introspector;

import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Eric Taix on 04/06/15.
 */
public class YamlObjectTest {

    @Test
    public void should_return_value_for_simple_level_attribut() {
        YamlObject yamlObject = YamlObject.from("sample: abc");
        assertThat(yamlObject.getField("sample")).isEqualTo("abc");
    }

    @Test
    public void should_return_null_if_field_does_not_exist() {
        YamlObject yamlObject = YamlObject.from("sample: abc");
        assertThat(yamlObject.getField("not_exist")).isNull();
    }

    @Test
    public void should_return_value_for_two_level_attribut() {
        YamlObject yamlObject = YamlObject.from("level1:\n  level2: def");
        assertThat(yamlObject.getField("level1.level2")).isEqualTo("def");
    }

    @Test
    public void should_return_value_for_n_level_attribut() {
        YamlObject yamlObject = YamlObject.from("level1:\n  level21:\n    level211: value211\n  level22:\n    level221: value221\n    level222: value222");
        assertThat(yamlObject.getField("level1.level21.level211")).isEqualTo("value211");
        assertThat(yamlObject.getField("level1.level22.level221")).isEqualTo("value221");
        assertThat(yamlObject.getField("level1.level22.level222")).isEqualTo("value222");
    }

    @Test
    public void should_return_hashmap_for_attribut_multiple_values() {
        YamlObject yamlObject = YamlObject.from("level1:\n  level21:\n    level211: value211\n  level22:\n    level221: value221\n    level222: value222");
        HashMap result = yamlObject.getField("level1.level22", HashMap.class);
        assertThat(result).isNotNull().isNotNull();
    }
}
