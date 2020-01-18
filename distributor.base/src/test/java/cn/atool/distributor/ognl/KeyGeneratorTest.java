package cn.atool.distributor.ognl;

import cn.atool.distributor.fortest.ForTestClazz;
import ognl.OgnlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.junit5.Test4J;
import org.test4j.tools.datagen.DataProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/12/20.
 */
class KeyGeneratorTest extends Test4J {

    @ParameterizedTest
    @MethodSource
    void buildMetas(String keyEl, String[] list) {
        List<String> result = KeyGenerator.buildMetas(keyEl);
        want.list(result).eqReflect(list);
    }

    static DataProvider buildMetas() {
        return new DataProvider()
                .data("abc", new String[]{"abc"})
                .data("${abc}", new String[]{"#abc"})
                .data("A${abc}B", new String[]{"A", "#abc", "B"})
                .data("$${abc}", new String[]{"$", "#abc"})
                .data("}${abc}", new String[]{"}", "#abc"})
                ;
    }

    @ParameterizedTest
    @MethodSource
    void illegalExpression(String keyEl) {
        try {
            KeyGenerator.buildMetas(keyEl);
            want.fail();
        } catch (RuntimeException e) {

        }
    }

    static DataProvider illegalExpression() {
        return new DataProvider()
                .data("${abc")
                .data("asfad#adf")
                .data("")
                .data((String) null)
                ;
    }

    Map<String, Object> context = new HashMap<String, Object>() {
        {
            this.put("user", new HashMap<String, Object>() {
                {
                    this.put("id", 123);
                    this.put("inner", new ForTestClazz("test", 456));
                }
            });
            this.put("outer", new ForTestClazz("outter", 12345));
            this.put("list", Arrays.asList("abc", "efg"));
        }
    };

    @Test
    void buildKey() throws OgnlException {
        String key = KeyGenerator.buildOgnlKey("${outer.number}ad$}fa${user.id}${user.inner.name}_${list[1]}", context);
        want.string(key).eq("12345ad$}fa123test_efg");
    }
}

