package cn.atool.distributor.ognl;

import cn.atool.distributor.serialize.SerializeFactory;
import ognl.Ognl;
import ognl.OgnlException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 构建唯一键
 *
 * @author darui.wu
 * @create 2020/1/13 5:08 下午
 */
public class KeyGenerator {
    /**
     * ognl表达式分解缓存
     */
    final static Map<String, List<String>> OGNL_EL_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据key值和切面方法参数，构造关键字
     *
     * @param keyEl
     * @param pjp
     * @return
     */
    public static String key(String keyEl, ProceedingJoinPoint pjp) {
        Map<String, Object> context = getMethodContext(pjp);
        try {
            if (keyEl == null || keyEl.trim().equals("")) {
                return argsMd5(SerializeFactory.defaultProtocol().toString(context));
            } else {
                return buildOgnlKey(keyEl, context);
            }
        } catch (Exception e) {
            throw new RuntimeException("build keyEl:" + keyEl + ", error:" + e.getMessage(), e);
        }
    }

    /**
     * 获取参数名称和对应参数值
     *
     * @param pjp
     * @return
     */
    static Map<String, Object> getMethodContext(ProceedingJoinPoint pjp) {
        MethodSignature signature = ((MethodSignature) pjp.getSignature());

        Object[] args = pjp.getArgs();
        String[] paramNames = signature.getParameterNames();
        if (paramNames == null || args == null) {
            return new HashMap<>();
        }
        Map<String, Object> context = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            context.put(paramNames[i], args[i]);
        }
        return context;
    }

    static String buildOgnlKey(String keyEl, Map<String, Object> context) throws OgnlException {
        List<String> metas = findCachedMetas(keyEl);
        StringBuilder buff = new StringBuilder();
        for (String meta : metas) {
            if (meta.startsWith("#")) {
                Object ognl = Ognl.parseExpression(meta);
                Object value = Ognl.getValue(ognl, context, (Object) null);
                buff.append(value);
            } else {
                buff.append(meta);
            }
        }
        return buff.toString();
    }

    /**
     * 根据key构建表达式解析出元数据列表
     *
     * @param keyEl
     * @return
     */
    public static List<String> findCachedMetas(String keyEl) {
        if (OGNL_EL_CACHE.containsKey(keyEl)) {
            return OGNL_EL_CACHE.get(keyEl);
        }
        synchronized (OGNL_EL_CACHE) {
            if (OGNL_EL_CACHE.containsKey(keyEl)) {
                return OGNL_EL_CACHE.get(keyEl);
            }
            List<String> metas = buildMetas(keyEl);
            OGNL_EL_CACHE.put(keyEl, metas);
            return metas;
        }
    }


    /**
     * 根据key构建表达式解析出元数据列表
     *
     * @param keyEl
     * @return
     */
    static List<String> buildMetas(String keyEl) {
        List<String> list = new ArrayList<>();
        if (keyEl == null || "".equals(keyEl.trim())) {
            throw new RuntimeException("expression can't be blank.");
        }
        if (keyEl.contains("#")) {
            throw new RuntimeException("expression can't contain character[#], please use ${variable}, expression:" + keyEl);
        }

        String tempStr = keyEl;
        while (true) {
            int start = tempStr.indexOf("${");
            if (start < 0) {
                if (!"".equals(tempStr)) {
                    list.add(tempStr);
                }
                break;
            }
            if (start > 0) {
                list.add(tempStr.substring(0, start));
            }
            tempStr = tempStr.substring(start + 2);
            int end = tempStr.indexOf("}");
            if (end < 1) {
                throw new RuntimeException("illegal expression: " + keyEl);
            } else {
                list.add("#" + tempStr.substring(0, end));
                tempStr = tempStr.substring(end + 1);
            }
        }
        return list;
    }

    final static MessageDigest md5 = initMd5();

    static MessageDigest initMd5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对参数列表md5取值
     *
     * @param args
     * @return
     */
    public static String argsMd5(String args) {
        byte[] bytes = md5.digest(args.getBytes());
        String hash = new BigInteger(1, bytes).toString(Character.MAX_RADIX);
        return hash;
    }
}
