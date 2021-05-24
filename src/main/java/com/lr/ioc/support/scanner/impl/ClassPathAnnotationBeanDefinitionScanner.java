package com.lr.ioc.support.scanner.impl;

import com.lr.ioc.annotation.Component;
import com.lr.ioc.annotation.Primary;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.annotation.Lazes;
import com.lr.ioc.support.annotation.Scopes;
import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.lr.ioc.support.scanner.AnnotationBeanDefinitionScanner;
import com.lr.ioc.support.scanner.BeanDefinitionScannerContext;
import com.lr.ioc.util.ClassAnnotationTypeMetadata;
import com.lr.ioc.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class ClassPathAnnotationBeanDefinitionScanner implements AnnotationBeanDefinitionScanner {

    private BeanDefinitionScannerContext context;

    @Override
    public Set<BeanDefinition> scan(BeanDefinitionScannerContext context) {
        this.context = context;

        Set<String> scanClassNameSet = new HashSet<>();
        //1.获取所有类全称
        context.getScanPackages().forEach((packageName) -> {
            Set<String> classNameSet = scanPackageClassNameSet(packageName);
            scanClassNameSet.addAll(classNameSet);
        });

        //2.构建类定义
        List<Class<?>> classList = new LinkedList<>();
        scanClassNameSet.forEach((className) -> {
            classList.add(ClassUtils.getClass(className));
        });

        //3.添加过滤
        Set<BeanDefinition> beanDefinitionSet = new HashSet<>();
        BeanNameStrategy beanNameStrategy = new DefaultBeanNameStrategy();
        classList.forEach((clazz) -> {
            ClassAnnotationTypeMetadata typeMetadata = new ClassAnnotationTypeMetadata(clazz);
            if (isTypeAnnotationMatch(typeMetadata, clazz)) {
                //3.1构建bean定义
                BeanDefinition beanDefinition = buildComponentBeanDefinition(clazz, typeMetadata, beanNameStrategy);
                beanDefinitionSet.add(beanDefinition);
            }
        });

        return beanDefinitionSet;
    }


    private Set<String> scanPackageClassNameSet(final String packageName) {
        Set<String> classNameSet = new HashSet<>();

        try {
            String packageDirName = packageName.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                // 文件处理
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    File file = new File(filePath);

                    // 递归处理下面的文件明细
                    if (file.isDirectory()) {
                        File[] files = file.listFiles();
                        if (files.length != 0) {
                            for (File entry : files) {
                                recursiveFile(packageName, entry, classNameSet);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IocRuntimeException(e);
        }

        return classNameSet;
    }

    private static void recursiveFile(String packageNamePrefix, File file, Set<String> classNameSet) {
        // 如果是文件
        if (file.isFile()) {
            String fileName = file.getName().split("\\.")[0];
            String className = packageNamePrefix + "." + fileName;

            classNameSet.add(className);
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                String dirName = file.getName();
                packageNamePrefix += "." + dirName;
                if (files.length != 0) {
                    for (File fileEntry : files) {
                        // 递归处理
                        recursiveFile(packageNamePrefix, fileEntry, classNameSet);
                    }
                }
            }
        }
    }

    private boolean isTypeAnnotationMatch(ClassAnnotationTypeMetadata typeMetadata,
                                          Class<?> clazz) {

        List<Class<? extends Annotation>> includes = context.getIncludes();
        List<Class<? extends Annotation>> excludes = context.getExcludes();

        if (null != typeMetadata.isAnnotatedOrMeta(includes)
                && null == typeMetadata.isAnnotatedOrMeta(excludes)) {
            return true;
        }

        return false;
    }

    private BeanDefinition buildComponentBeanDefinition(Class<?> clazz,
                                                        ClassAnnotationTypeMetadata typeMetadata,
                                                        BeanNameStrategy beanNameStrategy) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        beanDefinition.setLazyInit(Lazes.getLazy(clazz));
        beanDefinition.setScope(Scopes.getScope(clazz));
        beanDefinition.setSourceType(BeanSourceType.COMPONENT);
        Object id = typeMetadata.getDirectComponentAnnotationName(Component.class, "value");
        if (null == id) {
            beanDefinition.setId(beanNameStrategy.generateBeanName(beanDefinition));
        } else {
            if (StringUtils.isEmpty((String) id)) {
                beanDefinition.setId(beanNameStrategy.generateBeanName(beanDefinition));
            } else {
                beanDefinition.setId((String) id);
            }
        }
        if (clazz.isAnnotationPresent(Primary.class)) {
            beanDefinition.setPrimary(true);
        }

        return beanDefinition;
    }

}
